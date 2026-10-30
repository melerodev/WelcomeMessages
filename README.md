<h1 align="center">WelcomeMessages</h1>
<p align="center">
    <img alt="License" src="https://img.shields.io/github/license/melerodev/WelcomeMessages?style=for-the-badge&color=blue&labelColor=141417">
    <img alt="Latest version" src="https://img.shields.io/github/v/release/melerodev/WelcomeMessages?include_prereleases&sort=semver&style=for-the-badge&label=VERSION&labelColor=141417">
    <img alt="CI" src="https://img.shields.io/github/actions/workflow/status/melerodev/WelcomeMessages/ci.yml?style=for-the-badge&labelColor=141417">
    <img alt="Paper" src="https://img.shields.io/badge/Paper%20%2F%20Folia-1.20.6%20%E2%86%92%2026.3-2ecc71?style=for-the-badge&labelColor=141417">
    <a href="https://discord.gg/rMuWwZzCW2"><img alt="Discord" src="https://img.shields.io/badge/Discord-Join-5865F2?style=for-the-badge&logo=discord&logoColor=white&labelColor=141417"></a>
</p>

---

## Description

**WelcomeMessages** welcomes players when they join the server. Beyond "a message on join", it lets you
configure custom messages, placeholders, sounds, titles, action bar, actions (commands) and
**different profiles per world**, with special treatment for a player's first join.

Built on top of [Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).

## Features

- 💬 **Custom join and quit messages** for the whole server (`custom`, `vanilla` or `hidden` mode).
- ✨ **First join**: a different broadcast, private welcome, title and actions for new players.
- 📜 **Private welcome** in chat, with as many lines as you want.
- 🎬 **Titles and subtitles** with configurable timings, and an **action bar**.
- 🔊 **Sounds**: one for the joining player and an optional one for everyone else.
- ⚙️ **Actions**: `[console]`, `[player]`, `[message]`, `[broadcast]`, `[actionbar]`, `[title]`, `[sound]`.
- 🌍 **Per-world settings**: every world can have its own profile, and you can disable the plugin in some worlds.
- 🧩 Built-in **placeholders**, **PlaceholderAPI** and **MiniPlaceholders**; **MiniMessage** format and legacy codes.
- 🤫 `welcomemessages.silent` permission to join/leave without a broadcast (great for staff).
- 🔄 `/welcomemessages reload` and `/welcomemessages test` to preview.
- ⚡ **Folia** supported.

## Requirements

- **Paper** or **Folia** server, **1.20.6 to 26.3**.
- **Java 21** (Minecraft 1.20.6 – 1.21.x) or **Java 25** (Minecraft 26.x).
- Does not run on Spigot or on versions older than 1.20.6.
- Optional: [PlaceholderAPI](https://modrinth.com/plugin/placeholderapi), [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders).

Tested versions: 1.20.6, 1.21.1, 1.21.4, 1.21.8, 1.21.11, 26.1.2, 26.2 and 26.3.

## Installation

1. Download the `.jar` from [Releases](https://github.com/melerodev/WelcomeMessages/releases) (or build it, see below).
2. Put it in `plugins/` and restart the server.
3. Edit `plugins/WelcomeMessages/config.yml` and run `/welcomemessages reload`.

## Commands and permissions

| Command                                   | Description                                            | Permission                                 |
|-------------------------------------------|--------------------------------------------------------|--------------------------------------------|
| `/welcomemessages`                        | Help                                                   | `welcomemessages.command` (OP)             |
| `/welcomemessages reload`                 | Reloads `config.yml` and the language files            | `welcomemessages.command.reload` (OP)      |
| `/welcomemessages test [first-join]`      | Shows you the welcome of the world you are in          | `welcomemessages.command.test` (OP)        |
| —                                         | Join and leave without a broadcast                     | `welcomemessages.silent` (nobody by default, not even OPs) |

Alias: `/wm`.

## Placeholders

| Placeholder       | Value                                              |
|-------------------|----------------------------------------------------|
| `<player>`        | Player name                                        |
| `<displayname>`   | Display name (with prefixes from other plugins)    |
| `<uuid>`          | Player UUID                                        |
| `<world>`         | World the player joins in                          |
| `<online>`        | Online players                                     |
| `<max_players>`   | Max players                                        |
| `<unique_joins>`  | Unique players that have ever joined               |
| `%...%`           | Any PlaceholderAPI placeholder                     |

PlaceholderAPI: `%welcomemessages_unique_joins%`, `%welcomemessages_first_join%` (timestamp of the first join).

## Actions

```yaml
actions:
  - '[console] give <player> minecraft:bread 16'   # command from the console
  - '[player] spawn'                               # command as the player
  - '[message] <green>Hi <player>!'                # private message
  - '[broadcast] <gray><player> is back'           # message to everyone
  - '[actionbar] <yellow>Welcome!'                 # action bar
  - '[title] <gold>Hello;<gray>Subtitle'           # title;subtitle
  - '[sound] minecraft:entity.player.levelup;1;1'  # sound;volume;pitch
```

`actions` runs on every join; `first-join-actions` only the first time (in addition to `actions`).

## Per-world settings

`defaults` applies in every world. If you add a world under `worlds`, that profile **completely replaces**
`defaults` for players joining in that world. Worlds in `disabled-worlds` keep the vanilla messages.

```yaml
worlds:
  lobby:
    welcome:
      - <aqua>You are in the lobby. <gray>Use the compass to pick a game!
    actions:
      - '[player] spawn'
    title:
      enabled: true
      title: <aqua>Lobby
      subtitle: <gray>Welcome, <player>

disabled-worlds:
  - world_minigames
```

<details>
<summary>Default <code>config.yml</code> (<code>defaults</code> profile)</summary>

```yaml
defaults:
  enabled: true
  join-message:
    # custom = use `message`, vanilla = keep the default Minecraft message, hidden = no message
    mode: custom
    message: <dark_gray>[<green>+</green>]</dark_gray> <gray><player>
  first-join-message: <gold>✦ <yellow>Welcome <white><player></white> to the server for the first time! <gray>(#<unique_joins>)
  quit-message:
    mode: custom
    message: <dark_gray>[<red>-</red>]</dark_gray> <gray><player>
  delay-ticks: 10
  welcome:
    - ''
    - <gradient:#00C9FF:#92FE9D><bold>Welcome back, <player>!</bold></gradient>
    - <gray>There are <white><online></white> players online.
    - ''
  first-join-welcome:
    - ''
    - <gradient:#FFD700:#FF8C00><bold>Welcome to the server, <player>!</bold></gradient>
    - <gray>Type <yellow>/help</yellow> to get started.
    - ''
  action-bar: <green>Enjoy your stay, <player>!
  title:
    enabled: true
    title: <gradient:#00C9FF:#92FE9D>Welcome back</gradient>
    subtitle: <gray><player>
    fade-in: 10
    stay: 60
    fade-out: 20
  first-join-title:
    enabled: true
    title: <gold>Welcome!
    subtitle: <yellow>Have fun, <player>
    fade-in: 10
    stay: 60
    fade-out: 20
  sound:
    enabled: true
    sound: minecraft:entity.player.levelup
    volume: 1.0
    pitch: 1.0
  broadcast-sound:
    enabled: false
    sound: minecraft:block.note_block.pling
    volume: 0.6
    pitch: 1.6
  actions: []
  first-join-actions:
    - '[console] give <player> minecraft:bread 16'

disabled-worlds: []

# en_US or es_ES
language: en_US
```

</details>

## Building

```bash
./gradlew build
```

The jar ends up in `paper/build/libs/`. To try it on a local server:

```bash
./gradlew runServer                      # Paper 1.21.11
./gradlew runServer -PmcVersion=1.20.6   # any other version
```

## Support

Questions, suggestions or problems? Join our Discord:

<a href="https://discord.gg/rMuWwZzCW2"><img alt="Join our Discord" src="docs/assets/discord.png" width="300"></a>

You can also open an [issue](https://github.com/melerodev/WelcomeMessages/issues).

## License

[MIT No Attribution](LICENSE). Based on [milkdrinkers/Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).
