<h1 align="center">WelcomeMessages</h1>
<p align="center">
    <img alt="Licencia" src="https://img.shields.io/github/license/melerodev/WelcomeMessages?style=for-the-badge&color=blue&labelColor=141417">
    <img alt="Última versión" src="https://img.shields.io/github/v/release/melerodev/WelcomeMessages?include_prereleases&sort=semver&style=for-the-badge&label=VERSI%C3%93N&labelColor=141417">
    <img alt="CI" src="https://img.shields.io/github/actions/workflow/status/melerodev/WelcomeMessages/ci.yml?style=for-the-badge&labelColor=141417">
    <img alt="Paper" src="https://img.shields.io/badge/Paper%20%2F%20Folia-1.20.6%20%E2%86%92%2026.3-2ecc71?style=for-the-badge&labelColor=141417">
    <a href="https://discord.gg/rMuWwZzCW2"><img alt="Discord" src="https://img.shields.io/badge/Discord-%C3%9Anete-5865F2?style=for-the-badge&logo=discord&logoColor=white&labelColor=141417"></a>
</p>

---

## Descripción

**WelcomeMessages** da la bienvenida a los jugadores cuando entran al servidor. Más allá de "un mensaje al entrar",
te deja configurar mensajes personalizados, placeholders, sonidos, títulos, action bar, acciones (comandos) y
**perfiles distintos por mundo**, con un trato especial para la primera vez que alguien entra.

Construido sobre la plantilla [Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).

## Características

- 💬 **Mensajes personalizados** de entrada y salida para todo el servidor (modo `custom`, `vanilla` o `hidden`).
- ✨ **Primera entrada**: mensaje global, bienvenida privada, título y acciones distintas para jugadores nuevos.
- 📜 **Bienvenida privada** en el chat, con tantas líneas como quieras.
- 🎬 **Títulos y subtítulos** con tiempos configurables, y **action bar**.
- 🔊 **Sonidos**: uno para el jugador que entra y otro opcional para el resto.
- ⚙️ **Acciones**: `[console]`, `[player]`, `[message]`, `[broadcast]`, `[actionbar]`, `[title]`, `[sound]`.
- 🌍 **Configuración por mundo**: cada mundo puede tener su propio perfil, y puedes desactivar el plugin en algunos mundos.
- 🧩 **Placeholders** propios, **PlaceholderAPI** y **MiniPlaceholders**; formato **MiniMessage** y códigos legacy.
- 🤫 Permiso `welcomemessages.silent` para entrar/salir sin anunciarse (ideal para staff).
- 🔄 `/welcomemessages reload` y `/welcomemessages test` para previsualizar.
- ⚡ Compatible con **Folia**.

## Requisitos

- Servidor **Paper** o **Folia** de **1.20.6 a 26.3**.
- **Java 21** (Minecraft 1.20.6 – 1.21.x) o **Java 25** (Minecraft 26.x).
- No funciona en Spigot ni en versiones anteriores a 1.20.6.
- Opcional: [PlaceholderAPI](https://modrinth.com/plugin/placeholderapi), [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders).

Versiones probadas: 1.20.6, 1.21.1, 1.21.4, 1.21.8, 1.21.11, 26.1.2, 26.2 y 26.3.

## Instalación

1. Descarga el `.jar` desde [Releases](https://github.com/melerodev/WelcomeMessages/releases) (o compílalo, ver abajo).
2. Cópialo en `plugins/` y reinicia el servidor.
3. Edita `plugins/WelcomeMessages/config.yml` y ejecuta `/welcomemessages reload`.

## Comandos y permisos

| Comando                                   | Descripción                                            | Permiso                                    |
|-------------------------------------------|--------------------------------------------------------|--------------------------------------------|
| `/welcomemessages`                        | Ayuda                                                  | `welcomemessages.command` (OP)             |
| `/welcomemessages reload`                 | Recarga `config.yml` y los idiomas                     | `welcomemessages.command.reload` (OP)      |
| `/welcomemessages test [first-join]`      | Te muestra la bienvenida del mundo en el que estás     | `welcomemessages.command.test` (OP)        |
| —                                         | Entrar y salir sin mensaje global                      | `welcomemessages.silent` (nadie por defecto, ni OP) |

Alias: `/wm`.

## Placeholders

| Placeholder       | Valor                                              |
|-------------------|----------------------------------------------------|
| `<player>`        | Nombre del jugador                                 |
| `<displayname>`   | Nombre mostrado (con prefijos de otros plugins)    |
| `<uuid>`          | UUID del jugador                                   |
| `<world>`         | Mundo en el que entra                              |
| `<online>`        | Jugadores conectados                               |
| `<max_players>`   | Máximo de jugadores                                |
| `<unique_joins>`  | Jugadores únicos que han entrado alguna vez        |
| `%...%`           | Cualquier placeholder de PlaceholderAPI            |

PlaceholderAPI: `%welcomemessages_unique_joins%`, `%welcomemessages_first_join%` (timestamp de la primera entrada).

## Acciones

```yaml
actions:
  - '[console] give <player> minecraft:bread 16'   # comando desde consola
  - '[player] spawn'                               # comando como el jugador
  - '[message] <green>¡Hola <player>!'             # mensaje privado
  - '[broadcast] <gray><player> ha vuelto'         # mensaje a todos
  - '[actionbar] <yellow>¡Bienvenido!'             # action bar
  - '[title] <gold>Hola;<gray>Subtítulo'           # título;subtítulo
  - '[sound] minecraft:entity.player.levelup;1;1'  # sonido;volumen;pitch
```

`actions` se ejecuta en cada entrada; `first-join-actions` solo la primera vez (además de `actions`).

## Configuración por mundo

`defaults` se aplica en todos los mundos. Si añades un mundo en `worlds`, ese perfil **sustituye por completo**
a `defaults` para quien entre en ese mundo. Los mundos de `disabled-worlds` mantienen los mensajes vanilla.

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
<summary><code>config.yml</code> por defecto (perfil <code>defaults</code>)</summary>

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

## Compilar

```bash
./gradlew build
```

El jar queda en `paper/build/libs/`. Para probarlo en un servidor local:

```bash
./gradlew runServer                      # Paper 1.21.11
./gradlew runServer -PmcVersion=1.20.6   # cualquier otra versión
```

## Soporte

¿Dudas, sugerencias o problemas? Únete a nuestro Discord:

<a href="https://discord.gg/rMuWwZzCW2"><img alt="Únete a nuestro Discord" src="docs/assets/discord.png" width="300"></a>

También puedes abrir una [issue](https://github.com/melerodev/WelcomeMessages/issues).

## Licencia

[MIT No Attribution](LICENSE). Basado en [milkdrinkers/Minecraft-Plugin-Template](https://github.com/milkdrinkers/Minecraft-Plugin-Template).
