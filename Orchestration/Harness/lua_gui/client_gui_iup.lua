-- Lua GUI for Harness (IUP). Requires: luarocks install iup
-- Run: lua client_gui_iup.lua
-- Start harness first: cd Harness/python && python -m harness --port 15555

local ok, iup = pcall(require, "iuplua")
if not ok or not iup then
  print("IUP not found. Install with: luarocks install iup")
  os.exit(1)
end

local api = require("api")

local log_text = iup.text{
  expand = "YES",
  multiline = "YES",
  readonly = "YES",
  scrollbar = "YES",
}

local input_text = iup.text{
  expand = "HORIZONTAL",
  multiline = "YES",
  size = "200x60",
}

local model_list = iup.list{ expand = "HORIZONTAL", visible_items = 4 }
local model_label = iup.label{ title = "Model:" }

local function log_append(s)
  local v = log_text.value or ""
  log_text.value = v .. s .. "\n"
end

local function send_message()
  local content = (input_text.value or ""):match("^%s*(.-)%s*$")
  if content == "" then return end
  input_text.value = ""
  log_append("You: " .. content)
  local idx = model_list.value and tonumber(model_list.value)
  local model = (idx and model_list[idx]) or "lfm2"
  log_append("Assistant: ")
  iup.Refresh(iup.GetDialog(input_text))
  local reply, err = api.chat(content, model, false)
  if err then
    log_append("[Error: " .. err .. "]")
  else
    log_append(reply or "")
  end
  iup.Refresh(iup.GetDialog(input_text))
end

local function new_chat()
  api.new_chat()
  log_text.value = ""
  log_append("New chat.")
end

local function refresh_models()
  local models, err = api.list_models()
  if err then
    model_list[1] = "lfm2 (default)"
    return
  end
  model_list[1] = nil
  for i, m in ipairs(models or {}) do
    model_list[i] = m
  end
  if not models or #models == 0 then
    model_list[1] = "lfm2"
  end
end

local btn_send = iup.button{ title = "Send", action = send_message }
local btn_new = iup.button{ title = "New chat", action = new_chat }

input_text.k_any = function(self, c)
  if c == iup.K_CR or c == iup.K_BS then
    if c == iup.K_CR then
      send_message()
      return iup.IGNORE
    end
  end
  return iup.DEFAULT
end

local box = iup.vbox{
  iup.hbox{ model_label, model_list, "MARGIN=2" },
  iup.label{ title = "Chat (Enter to send):" },
  log_text,
  input_text,
  iup.hbox{ btn_send, btn_new, "MARGIN=2" },
  gap = 4,
  margin = "8x8",
}

local dlg = iup.dialog{ box; title = "Harness — Lua GUI (Ollama)"; size = "HALFxHALF" }

refresh_models()
log_append("Harness at " .. api.url .. " — type and press Enter to send. /new = New chat.")

iup.Show(dlg)
iup.MainLoop()
