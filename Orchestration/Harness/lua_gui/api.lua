-- Harness API client (Lua). Requires: luarocks install luasocket
local http = require("socket.http")
local ltn12 = require("ltn12")

local HARNESS_URL = os.getenv("HARNESS_URL") or "http://127.0.0.1:15555"

local function escape_json(s)
  return (s:gsub("\\", "\\\\"):gsub('"', '\\"'):gsub("\n", "\\n"):gsub("\r", "\\r"))
end

local function get(path)
  local b = {}
  local res, code = http.request{
    url = HARNESS_URL .. path,
    sink = ltn12.sink.table(b),
  }
  if code ~= 200 then
    return nil, "HTTP " .. tostring(code) .. " " .. table.concat(b)
  end
  return table.concat(b)
end

local function post(path, body)
  local b = {}
  local res, code = http.request{
    url = HARNESS_URL .. path,
    method = "POST",
    headers = { ["Content-Type"] = "application/json" },
    source = ltn12.source.string(body),
    sink = ltn12.sink.table(b),
  }
  if code ~= 200 and code ~= 201 then
    return nil, "HTTP " .. tostring(code) .. " " .. table.concat(b)
  end
  return table.concat(b)
end

-- Minimal JSON decode for our API responses (content, error, models).
local function json_decode(s)
  if not s or s == "" then return nil end
  local content = s:match('"content"%s*:%s*"([^"]*)"')
  if content then
    content = content:gsub("\\n", "\n"):gsub("\\\"", '"'):gsub("\\\\", "\\")
    return { content = content }
  end
  local err = s:match('"error"%s*:%s*"([^"]*)"')
  if err then
    err = err:gsub("\\n", "\n"):gsub("\\\"", '"'):gsub("\\\\", "\\")
    return { error = err }
  end
  local models = s:match('"models"%s*:%s*%[([^%]]*)%]')
  if models then
    local list = {}
    for name in models:gmatch('"([^"]+)"') do
      list[#list + 1] = name
    end
    return { models = list }
  end
  if s:match('"ok"%s*:%s*true') then
    return { ok = true }
  end
  return {}
end

return {
  url = HARNESS_URL,
  get = get,
  post = post,
  escape_json = escape_json,
  json_decode = json_decode,
  list_models = function()
    local raw, err = get("/api/models")
    if not raw then return nil, err end
    local data = json_decode(raw)
    if data and data.error then return nil, data.error end
    return data and data.models or {}
  end,
  chat = function(content, model, stream)
    stream = stream == nil and false or stream
    local body = '{"content":"' .. escape_json(content) .. '","stream":' .. tostring(stream)
    if model and model ~= "" then
      body = body .. ',"model":"' .. escape_json(model) .. '"'
    end
    body = body .. "}"
    local raw, err = post("/api/chat", body)
    if not raw then return nil, err end
    local data = json_decode(raw)
    if data and data.error then return nil, data.error end
    return data and data.content or ""
  end,
  new_chat = function()
    local raw, err = post("/api/new_chat", "{}")
    if not raw then return nil, err end
    return true
  end,
}
