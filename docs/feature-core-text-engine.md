# feature/core-text-engine

Branch for the core text engine only. Created before any engine code.

## In scope

- REQ-01 Main menu: intro text, new game, high scores, exit
- REQ-02 Easy, Normal, and Hard. Each sets shift length, scenario time limit, and victim count
- REQ-03 Global shift timer. Each submitted command costs 1 minute. Blank lines do not
- REQ-04 `help` prints commands for the current screen, plus difficulty and time left when a shift is active

Provisional scales (change in `Difficulty`):

- Easy: 60 min shift, 20 min scenario limit, 2 victims
- Normal: 45 min shift, 15 min scenario limit, 3 victims
- Hard: 30 min shift, 10 min scenario limit, 4 victims

When the timer hits 0, the shift ends with one failure line and returns to the menu. The full casualty summary is REQ-13.

## Out of scope

Station search, truck map, scenario pool, rescues, letter grades, and the leaderboard (REQ-05 through REQ-15). The scores menu item only reports that no shifts are recorded yet.
