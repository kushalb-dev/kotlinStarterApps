# SecretMessage

This app was built with the purpose of learning how to navigate between different screens utilizing the latest
navigation-compose library and jetpack compose. Learning to do this project involved being able to handle the
latest change in jetpack compose in the form of navHost, which allowed me to navigate between different screens
through the use of code only.

This app utilizes three components that are displayed as separate screens:

    -> Welcome Screen: Starting screen of the app
    -> Enter Message Screen: Allows user to enter a message to be encrypted
    -> Result Screen: Displays the encrypted message

The message is passed between the enter message and result screen through the use of arguments and backStack.
The message is packaged into a URL which directs to the display results screen. The url of each screen is
declared in the Screen class.