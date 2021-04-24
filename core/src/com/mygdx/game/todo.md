### collision

done: level collision pending: enemy collision, weapon collision, particle collision, whatever

should consider using the tiles themselves to store collision yes/no and save the extra work of an object layer.

### animation

done: idle vs. moving pending: L vs R; hit animation + blink effect;

## levels / map

done: collision and some basic markings in darkplaces map pending: **spawn points; transitions between levels via
stairs;** transitions from room to room?

## camera / controls

pending: attacking; smart handling of multiple direction keys

### re: direction keys:

if the order of keys events is `keyDown UP -> keyDown RIGHT -> keyUp RIGHT` then we
want `Direction.UP -> Direction RIGHT -> direction UP`.

At present it just stops. Which makes me think that just checking what's pressed is a better way than the input
listener.

### UI

pending: everything, but mainly want some debug stuff, a HUD; no need for main menu right now

special attack button? could be fun

### game

pending: actual combat, like players taking damage


## rough plan

• spawn points for players and enemies

• player takes damage from touching enemies
• enemies wander & collide w/ level geometry

• player attacks with weapon
• a few items -- gold (score), food (health)
• enemies drop items sometimes, mostly gold

• abrupt level transitions, i.e. stand on stairs, go down. yes backtracking?

• enemies attack????

• doors?

