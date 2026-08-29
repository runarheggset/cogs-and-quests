# Running the server

The server runs the same pinned pack as everyone's client, minus client-only
mods (minimap, Embeddium, etc. are marked `side = "client"` and get skipped).

## One-time setup (Java 17 required)

```sh
mkdir server && cd server

# 1. Forge server (version must match pack.toml [versions])
curl -LO https://maven.minecraftforge.net/net/minecraftforge/forge/1.20.1-47.4.23/forge-1.20.1-47.4.23-installer.jar
java -jar forge-1.20.1-47.4.23-installer.jar --installServer

# 2. packwiz-installer (syncs mods from the repo's pack.toml)
curl -LO https://github.com/packwiz/packwiz-installer-bootstrap/releases/latest/download/packwiz-installer-bootstrap.jar
java -jar packwiz-installer-bootstrap.jar -g -s server /path/to/cogs-and-quests/pack.toml

# 3. Accept EULA, set memory
echo "eula=true" > eula.txt
printf -- "-Xms2G\n-Xmx4G\n" > user_jvm_args.txt

./run.sh nogui
```

## After it starts

- `/gamerule playersSleepingPercentage 50` (one of two sleepers skips night)
- `server.properties`: set `motd`, consider `view-distance=8` to keep the
  8 GB client happy in render-heavy areas.

## Updating the server to a new pack version

```sh
cd /path/to/cogs-and-quests && git pull
cd ../server && java -jar packwiz-installer-bootstrap.jar -g -s server /path/to/cogs-and-quests/pack.toml
```

packwiz-installer adds/removes/updates exactly to the pinned index — the server
can never drift from the repo.

## Letting your friend in

Port-forward 25565, or skip router pain entirely with Tailscale on both
machines (he connects to your tailnet IP) or playit.gg for a public address.

## Kubernetes instead

`k8s/cogsquests.yaml` deploys the same server as a StatefulSet on the cluster
(same shape as the chunkcolony one): itzg/minecraft-server with `PACKWIZ_URL`
pointed at this repo's `pack.toml` **at a git tag**, so the server converges to
the exact pinned mod set on every boot and client-only mods are skipped
automatically.

```sh
kubectl apply -f k8s/cogsquests.yaml
kubectl -n cogsquests logs -f statefulset/cogsquests   # first boot ~10-20 min
```

- NodePort **30566** (30565 is chunkcolony's). Router-forward 25565/tcp there.
- Updating the server = push a new tag, bump `PACKWIZ_URL`, re-apply,
  `kubectl -n cogsquests rollout restart statefulset/cogsquests`.
- `playersSleepingPercentage 50` is applied via `RCON_CMDS_STARTUP`; no manual
  gamerule needed.
- **The repo must be public** for the raw URL to resolve.

**Private repo alternative:** once the CurseForge file is approved, swap the
`PACKWIZ_URL` env for `MOD_PLATFORM: AUTO_CURSEFORGE` + `CF_SLUG:
cogs-and-quests` + `CF_FILE_ID: <id>` and a `CF_API_KEY` from
console.curseforge.com in a k8s Secret. Downside: the CF manifest has no
client/server markers, so the six client-only mods must be listed in
`CF_EXCLUDE_MODS` by hand.

## The seed

`20260829` (the day the pack was finished). Chosen by generating eight
candidate worlds under the pack's exact Terralith version and scoring the
spawns over RCON; this one had river/forest/mountain/village/ocean/ruined
portal all within ~450 blocks of spawn, on land. Already wired into the k8s
manifest as `SEED`; for a manual server put `level-seed=20260829` in
server.properties before first boot.
