# MinecraftPlayerdataInventoryToCommand

## Description

Extracts the inventory of a minecraft player from the given .dat file in the playerdata directory. It generates a command that has to be put into a command block (because of its length). The block then has to be powered via a redstone signal. The nearest player is given a chest, that contains the inventory of the player. In case the player held more items than fitting in a chest multiple commands are generated.

## Running
* Windows: `mvnw compile exec:java -Dexec.args="<uuid>.dat"`
* Linux: `./mvnw compile exec:java -Dexec.args="<uuid>.dat"`

Example:
`./mvnw compile exec:java -Dexec.args="minecraft/world/playerdata/2dc70386-973e-491d-8122-7603ea8619e5.dat`
