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
