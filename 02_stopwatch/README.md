# Stopwatch

This app uses components inside the main activity to display and update the state of the stopwatch. The stopwatch
utilizes mainly three states:

    -> timeMilSecs: The time tracked (in millis) by 
    the stopwatch
    -> isRunning: The running state of the stopwatch
    -> wasRunning: The previous running state of the
    stopwatch, in case the app loses focus

The app also contains three buttons (Start, Stop, Reset), which change the state of the state variables mentioned above.
Besides these buttons, the app also changes state automatically when the app loses focus. The implementation of the
running state of the app and the state where the app loses/regains focus is implemented using two effects:

    -> Launched Effect: This acts as a co-worker thread 
    that runs in parallel with the main thread. It 
    updates the timeMilSecs variable when the isRunning
    state is true. This was used so as to not make
    the main thread responsible for tracking the time.

    -> Disposable Effect: This effect, as the app says
    is used to add an observer for when the app goes in
    and out of focus. The reason for this being disposable
    is that it needs to remove the observer when the composable
    is destroyed, such as when the phone is rotated. If not,
    having a lot of observers would make the app laggy.
    