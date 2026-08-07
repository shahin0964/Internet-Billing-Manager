import re

with open("app/src/main/java/com/example/ui/viewmodel/IspViewModel.kt", "r") as f:
    content = f.read()

content = content.replace('''        todayCollectionAmount = repository.getCollectedAmountForDate(todayStr).stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )

        todayCollectionAmount = repository.getCollectedAmountForDate(todayStr).stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )''', '''        todayCollectionAmount = repository.getCollectedAmountForDate(todayStr).stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )''')

with open("app/src/main/java/com/example/ui/viewmodel/IspViewModel.kt", "w") as f:
    f.write(content)
