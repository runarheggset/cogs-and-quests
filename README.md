# Cogs & Quests

A Vanilla+ modpack for two: Create-powered factories on one side, YUNG dungeons,
mythic mounts and Farmer's Delight cooking on the other. Built for Minecraft
**1.20.1 / Forge**, tuned to run on an 8 GB machine (allocate 4 GB).

Managed with [packwiz](https://packwiz.infra.link/): every mod is pinned to an
exact CurseForge file + hash in `mods/*.pw.toml`, so builds are fully
reproducible. CI exports a CurseForge-importable zip on every push and attaches
it to a GitHub release on every tag.

## Getting the pack (players)

Grab the latest `Cogs & Quests-*.zip` from
[Releases](../../releases) and import it in the **CurseForge app**
(Create Custom Profile → Import) or **Prism Launcher** (Add Instance → Import).

Set the instance's memory to **4096 MB**. Client-only mods (minimap, Jade,
AppleSkin, Embeddium…) may differ per player — adding Oculus + shaders locally
is fine.

## Working on the pack

Requirements: Go, then `go install github.com/packwiz/packwiz@latest`.
Run everything from the repo root.

| Task | Command |
|---|---|
| Add a mod | `packwiz curseforge add <slug-or-url>` |
| Remove a mod | `packwiz remove <name>` |
| Update one mod | `packwiz update <name>` |
| Update everything | `packwiz update --all` |
| Hold a mod back from updates | `packwiz pin <name>` (undo: `packwiz unpin`) |
| Rebuild the index after manual edits | `packwiz refresh` |
| Build the CurseForge zip locally | `packwiz curseforge export` |
| List everything | `packwiz list` |

`<name>` is the metadata filename in `mods/` without `.pw.toml`.

Updates never happen implicitly: a mod only moves when you run `update`, and
`pin` protects specific mods even from `update --all`. Commit the changed
`.pw.toml` + `index.toml` + `pack.toml` and CI does the rest.

You can also run updates from GitHub: **Actions → Update mods → Run workflow**
(one mod or `all`) — it opens a PR with the version-bump diff for review.

## Test-driving local changes

For a quick loop without exporting: `packwiz serve` hosts the pack at
`http://localhost:8080/pack.toml`, and a Prism/MultiMC instance with
[packwiz-installer](https://packwiz.infra.link/tutorials/installing/packwiz-installer/)
as a pre-launch step pulls every change on launch.

## Releasing

```sh
git tag v0.2.0 && git push --tags
```

CI builds the zip and creates a GitHub release with it attached.

## Publishing to CurseForge (later)

The exported zip is exactly what CurseForge expects for a modpack project:
create a project on curseforge.com, upload the zip from the release. When we
want to automate it, add a step using the CurseForge Upload API with a
`CURSEFORGE_TOKEN` repo secret.

## Ground rules

- **1.20.1 Forge stays** until every mod we care about has a stable successor
  target — version bumps are a deliberate, all-at-once migration.
- Keep it Vanilla+: additions should look and feel like they could be vanilla.
- Two-wave rule for new mods: add, play a session on the 8 GB machine, keep.
- No sleep-vote mod needed: set `/gamerule playersSleepingPercentage 50` on the
  server so one of two sleepers skips the night.
