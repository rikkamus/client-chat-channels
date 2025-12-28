# Client Chat Channels
[![CurseForge: Client Chat Channels](https://img.shields.io/badge/CurseForge-Client_Chat_Channels-f16436?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/client-chat-channels)
[![Modrinth: Client Chat Channels](https://img.shields.io/badge/Modrinth-Client_Chat_Channels-00af5c?logo=modrinth)](https://modrinth.com/mod/client-chat-channels)
[![MIT License](https://img.shields.io/badge/License-MIT-blue)](https://github.com/rikkamus/client-chat-channels/blob/-/LICENSE)

A client-side Minecraft mod that adds chat channels to the game.

## Channels
### 🌍 Global
Works like vanilla chat - all players on the server can see your messages (unless otherwise configured by the server).

### 🔊 Local
Messages are only sent to players within a specified distance from you.

### ✉️ Direct
Messages are sent exclusively to one or more specific players.

## How It Works
When using a channel other than global, the mod automatically converts your chat messages into private messages using the <code>/&zwj;msg</code> command and sends them only to the intended recipients.

## Commands
You can use commands to switch between different channels:

| Command                                                        | Usage                                                                                                                                 |
|----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| <code>/&zwj;channel&nbsp;global</code>                         | Switches to the global channel.                                                                                                       |
| <code>/&zwj;channel&nbsp;local&nbsp;&lt;radius&gt;</code>      | Switches to the local channel. If the radius is omitted, the default radius will be used.                                             |
| <code>/&zwj;channel&nbsp;direct&nbsp;&lt;recipients&gt;</code> | Switches to the direct channel. If recipients are omitted, the nearest player will be selected. Recipients should be space-separated. |
| <code>/&zwj;channel&nbsp;status</code>                         | Displays information about the currently selected channel.                                                                            |

## Hotkeys
You can also use hotkeys to quickly switch between channels:

| Channel / Action | Default Hotkey |
|------------------|----------------|
| Global           | U              |
| Local            | I              |
| Direct           | O              |
| Show status      | *Not bound*    |

## Configuration
- Channel indicators and the default range of the local channel can be customized using [Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config).
- Hotkeys can be configured in Minecraft's key binding options.

## Dependencies
### NeoForge
- [Cloth Config (optional)](https://www.curseforge.com/minecraft/mc-mods/cloth-config)

### Fabric
- [**Fabric API (REQUIRED)**](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
- [Cloth Config (optional)](https://www.curseforge.com/minecraft/mc-mods/cloth-config)
- [Mod Menu (optional)](https://www.curseforge.com/minecraft/mc-mods/modmenu)

## Building
To build the project, run `gradlew build` in the project's root directory.
