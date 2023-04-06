# Pinger Android Code Challenge

## Solution - Description of changes

1. The project was updated to use MVI (Model-View-Intent) architecture approach as you can check the
   file **intent/PageSequenceIntent** it contains the intents for the view to the ViewModel
2. In the previous version of the app, we were using the Presenter pattern to handle the app's
   business logic. However, in this update, I have migrated the app to use a **ViewModel**, you can
   check the file **viewmodel/PageSequenceViewModel**.The ViewModel architecture component is a
   recommended approach for handling the app's business logic in Android apps. It allows us to store
   and manage UI-related data in a lifecycle-conscious manner, and it also eliminates the need to
   manage state restoration manually. By using ViewModel, we can ensure that our app is more
   efficient, maintainable, and scalable.
3. I have migrated the app to use the Repository pattern and Flow instead of RxJava. This change
   improved separation of concerns: With Repository pattern, we can separate the data operations
   from the UI components. The using of Flow and Repository provides a more efficient, maintainable,
   and scalable app that is better suited for handling data operations, including network requests.
4. The view layer was updated to use **Android Compose** instead of XML views. Android Compose
   provides better performance than traditional XML and integrates seamlessly with Kotlin, also it
   makes easier to build and maintain UI code.
5. In this update, I have added the **Koin** dependency injection library to simplify the dependency
   injection process and improve the creation of unit tests.
6. Due to the good architecture, dependency injection, and separation of concerns, it was possible
   to add unit testing to the ViewModel and the Repository files, you can check the files: **PageSequenceRepositoryTest** and **PageSequenceViewModelTest**

In conclusion, the app was updated to an architecture that promotes code reusability, improves
testability, and integrates seamlessly with Kotlin and new Android development libraries. However,
it could be possible to add or improve a few more things like a more robust dependency injection
like **Hilt** or add unit testing to the view layer and use some framework like **Espresso**.

-----

This repo contains code that solved the following challenge,
> Please create an Android app that downloads a text file, parses the contents, and displays the results. You may retrieve the file [here](https://raw.githubusercontent.com/cplachta-pinger/android_coding_challenge/master/Apache.log). The file is a standard Apache web server access log. We’re interested in knowing the most common three-page sequences in the file. A three-page sequence is three consecutive requests from the same user. In case it helps simplify your processing, you can assume the following about the contents:
> - Log entries are in ascending chronological order
> - An IP address is unique per user
>
> Please present the results in descending order by sequence frequency, so the most common sequence appears first and the least common appears last. Each sequence should display the pages that make up the sequence and the number of times that the sequence appears.
>
> Example
>
> Based on the following sample data, sequence “Page 1, Page 2, Page 3” is the most common. It appears twice in the results:
>
> User A: Page 1
>
> User B: Page 1 User B: Page 2 User B: Page 3 User B: Page 2
>
> User A: Page 2 User A: Page 3 User A: Page 4 User A: Page 1 User A: Page 2

## Objective

1. Update this code to use more recent libraries and patterns.
    - Convert the XML views to use Compose.
    - Convert RxJava usage to Flow
    - Update anything else you see fit

2. The current code used an MVP (Model-View-Presenter) architecture pattern to solve the challenge
   above. Convert the architecture to use an MVI  (Model-View-Intent) approach.

## Return

Return to us either as a Public Repo or .zip of the android studio project (You may need to share a
google drive link if the project is too large).

Please include a summary of your changes. What do you think went well? What do you think you could
have done better?

Thank you.
