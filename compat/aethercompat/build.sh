#!/bin/bash
# Builds mods/cogsquests-aethercompat-<ver>.jar. Needs a JDK 17 on PATH plus an installed
# Forge 1.20.1 server ($FORGE_DIR/libraries) and the Aether jar, for the compile classpath.
# Nothing here calls obfuscated Minecraft members, so no ForgeGradle/refmap is required.
set -e
cd "$(dirname "$0")"
FORGE_DIR=${FORGE_DIR:?set FORGE_DIR to a Forge 1.20.1 server install}
AETHER_JAR=${AETHER_JAR:?set AETHER_JAR to the aether-1.20.1-*.jar}
VER=$(grep -oE '^version = "[^"]+"' res/META-INF/mods.toml | cut -d'"' -f2)
L=$FORGE_DIR/libraries
CP="$(find $L -name 'server-1.20.1-*-srg.jar'):$(find $L -name 'forge-1.20.1-*-universal.jar'):$(find $L -name 'mixin-0.8.5.jar'):$(find $L -name 'slf4j-api-*.jar' | head -1):$(find $L -name 'fmlcore-*.jar' | head -1):$(find $L -name 'javafmllanguage-*.jar' | head -1):$AETHER_JAR"
rm -rf classes && mkdir classes
javac --release 17 -proc:none -cp "$CP" -d classes $(find src -name '*.java')
cp -r res/* classes/
(cd classes && jar cfm "../../../mods/cogsquests-aethercompat-$VER.jar" ../manifest.txt .)
rm -rf classes
echo "built mods/cogsquests-aethercompat-$VER.jar"
