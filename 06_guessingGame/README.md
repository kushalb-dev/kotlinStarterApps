# Guessing Game

This game involves two screens:

    -> Game Screen: This screen contains UI that is fed data by the GameViewModel to display the current state of the game.
    -> Result Screen: This screen contains a simple UI that shows the correct word and the result of the game.

The navigation between screens is handled through NavHost and navController. The screen changes either if the player
wins or loses, the criteria for which is described in the GameViewModel.

This project helped me polish my navigation handling even further, as well as helped me to internalize the act of 
message passing between screens using serializable objects.