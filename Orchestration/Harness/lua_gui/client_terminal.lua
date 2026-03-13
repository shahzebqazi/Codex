#!/usr/bin/env lua
-- Terminal chat client for the Harness API (Ollama backend).
-- Usage: lua client_terminal.lua
-- Start harness first: cd Harness/python && python -m harness --port 15555

local api = require("api")

local function printf(...)
  io.write(string.format(...))
  io.flush()
end

local function list_models()
  local models, err = api.list_models()
  if err then
    printf("Models error: %s\n", err)
    return nil
  end
  return models
end

local function main()
  printf("Harness client (terminal) — %s\n", api.url)
  local models = list_models()
  local model = "lfm2"
  if models and #models > 0 then
    for _, m in ipairs(models) do
      if m == "lfm2" or m:match("^lfm2") then
        model = m
        break
      end
    end
    printf("Models: %s (using %s)\n", table.concat(models, ", "), model)
  else
    printf("Using default model: %s\n", model)
  end

  printf("\nCommands: /new = new chat, /quit = exit, /models = list models\n\n")

  while true do
    printf("You> ")
    local line = io.read("*l")
    if not line then break end
    line = line:match("^%s*(.-)%s*$") or ""

    if line == "/quit" or line == "" then
      if line == "/quit" then break end
    elseif line == "/new" then
      api.new_chat()
      printf("New chat.\n")
    elseif line == "/models" then
      list_models()
    else
      printf("Assistant> ")
      local reply, err = api.chat(line, model, false)
      if err then
        printf("[Error: %s]\n", err)
      else
        printf("%s\n", reply or "")
      end
    end
  end
  printf("Bye.\n")
end

main()
