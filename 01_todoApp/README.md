# TODO App

This app was built using jetpack compose. All the required code is present in the [Main Activity](https://github.com/kushalb-dev/kotlinStarterApps/blob/main/01_todoApp/app/src/main/java/com/starterKotlin/todolist/MainActivity.kt)
file. The outline of the activity is as follows:

    * Top Bar (containing the app name)
    * Recycler View (displaying the todo list)
    * Bottom Bar (containing the add todo text field
    and relevant buttons)

The architecture of the app is as follows:

## TodoApp() Composable
This contains the root layer of the app. In this composable, all the state is stored. The relevant states include:
    
    1. List of Todo Items: The list of todo items needs to be 
    tracked because these can be updated in a number of ways
    from multiple levels of the app. Hence, the root composable
    needs to keep track to update all the children composables.

    2. Current Input State: It needs to be tracked so that when
    the child composable button to add todo is called, it adds the
    latest task description.

## BottomBar() Composable
This contains the text field to enter the details of the task, an add task button and a button
to remove all 'done' tasks. The listeners of these buttons and text fields are propagated to the root
composable so that the [RecyclerView](#recyclerview-composable) can update the state of the displayed
todo items.

## RecyclerView() Composable
This contains the lazy column where the card for each todo item is displayed. Each card contains a 
textview and a checkbox to display todo-detail and todo-status. The listeners of the checkbox are also propagated
from the cardView composable to the recyclerView composable and then finally to the root composable.
