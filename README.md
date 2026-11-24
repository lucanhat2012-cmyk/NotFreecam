# NotFreecam

A standalone client-side detached-camera mod for **Minecraft Java 26.3** using Fabric.

## Controls

- **F4** — Toggle freecam.
- **F6** — Open settings.
- **WASD** — Move the camera.
- **Space / Shift** — Move up / down.
- **Sprint** — Temporary 2.5x movement boost.
- In settings, click the speed button to cycle camera speed.

The real player remains at their original position while the camera moves independently. Press F4 again to return to the player.

## Multiplayer limitation

This is a camera mod, not a remote-interaction exploit. Multiplayer servers validate block breaking, attacks, and item use against the real player's position and normal server rules. Looking at something from far away does not make it interactable from that distance. Normal interactions remain available when the target is within the server-authorized range of the real player.

## Build

Requires Java 25 and Gradle 9.6. Run `gradle build`; the mod jar is produced in `build/libs/`.
