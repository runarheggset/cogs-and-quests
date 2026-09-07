# Aether / MobStacker compat

Why this exists: MobStacker 1.0.14b runs its display-name update on every mob
load, and for any mob that owns a boss bar it ends in `setCustomName(null)`.
The Aether's Slider and Valkyrie Queen override `setCustomName` to mirror the
name into their synced boss-name entity data, so that becomes a null
`Component`, and every client that loads the boss is disconnected with
"Received unexpected null component". MobStacker's ignore lists don't gate
that path (the only guard is a hardcoded cobblemon/pixelmon check).

The two mixins here make `setBossName(null)` a no-op on those bosses. The mod
registers nothing on the network, so only the server strictly needs it; the
pack ships it to everyone for consistency. Rebuild with `build.sh`.
