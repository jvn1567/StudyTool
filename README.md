# StudyTool
This program allows a user to participate in various study activities, including reviewing flashcards, taking multiple-choice quizzes, and creating their own customized set of flashcards. The highest scores for quizzes will be recorded and saved between separate launches of the program. The GUI is built using JavaFX.

# Flashcards
https://im.ezgif.com/tmp/ezgif-1-ea4668ef3c43.mp4
The program user can review flashcard sets, which are read from .txt files in the "flashcards" folder. These files can be created and modified so long as they follow the standardized format. An example file is provided. The user can click the card in order to flip between its front and back sides. If the user wishes to continue reviewing the card, they can press the "keep" button, which will put it at the bottom of the deck for review again. The "toss" button will remove it entirely, and can be used when the user feels they no longer need to review the card.

# Custom Cards

A customizable set of cards named "Custom Cards" is provided to the user, and the contents of the card deck can be modified. When clicking the "Edit Custom" button, the user will be able to see all cards currently in the deck in a ListView and choose to edit or delete any existing cards, or add entirely new cards.

# Quizzes

Multiple choice quizzes can also be taken, and their information is also read from .txt files in the "multiplechoice" folder. These files can also be created or modified as long as they fit the standardized format. Several example files are provided. The number of correct questions will be tracked at the bottom of the window, so the user can get immediate feedback on their answers. Quiz scores will be recorded and can be viewed with the "Scores" button. New quizzes or improved scores will update the list of scores, which will be saved to a file and read on launch. The user can also retake a selected quiz directly from the scores page.
