# CatChat

This app was built for the purpose of learning how to navigate using three of the navigation UI options:

    -> Navigation Drawer: Navigation Panel on the side of screen that appears when we click its button
    on the top left corner.
    -> Bottom Nav Bar: A navigation bar that always appears on the bottom of the screen and allows switching
    between two options: Home and About
    -> Menu Options: A menu item that appeats on the top right of the screen that allows easy access to settings
    screen.

Instead of just using a composable to display the home screen where dummy mail items are displayed, I used a 
viewModel for the <i>Home Screen</i> so that the states are persistent even between screen changes. Using
such a viewModel made it very easy for me to save the state of the screen as the lifecycle of the viewmodel is
tied to the app session, not the session of the screen.