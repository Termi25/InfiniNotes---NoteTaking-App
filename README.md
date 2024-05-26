<img src="/app/src/main/res/drawable/design_logo_aplicatie_centratnobg.png" alt="InfiniNotes Logo" width="100px"/>

# InfiniNotes

<p color=red>**Warning! This project was made for a Bachelor's Degree Thesis and may prove to still have unresolved jank as well as a lot of variables names and app UI in Romanian, not English.**
Also, for the weather component to work, you need to add an assets folder in which there should be a file named "cheie.txt" with an OpenWeather API Key.</p>

## Description
This is an Android Notetaking app with in-built functionality for Android Auto (works only in Debug mode due to current Android Auto regulations).
Additionally, it includes weather forecast and reminder functionalities, as well as list making capabilities. 
Other features of note are dezactivation of weather and internet use on only the app, as well as a automatic cleanup of checked notes, lists or list elements at 3 different intervals (1, 7 or 30 days).

The Android Auto feature is that the app display all notes with reminder in descending order that have not happened yet. This functionality was thought not a feature for Android Auto, but an external device with Adnroid Auto for ease of use and as a desk companion (i.e. a Raspberry Pie with a 7 inch Screen and running an Android Auto emulator). As such, I do not recommend using the given code of that part elsewhere except for the creation method for building in app support instead of a separate module (the files pertaining to this part are in the main folder of the app module, **not** the appautomotive module, which is for Android Automotive testing).
