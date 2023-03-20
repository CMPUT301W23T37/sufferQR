# RegisterPage
|Responsibilities|Collaborators|
|:-----:|:-----:|
| Displays the registration page UI| Dashboard|
| Check the user input for email and username|Firebase database||
| Generate a random string consisting of capital alphabet numbers for a user's QR code ID||
| Save user info onto the firebase database||
| Close the registration page after saving the user information||
| user input data validation||

# SearchPlayer
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Find others users and access their profile|Firebase database|
|including their Qrcode Record|DrawerBase|

# LeaderBoard
| Responsibilities |Collaborators|
|:-----:|:-----:|
|Displays the leaderboard by inflating the activity LeaderBoardBinding|Firebase database|
|Show top scoreboards|DrawerBase|
|Allow user to choose regions||

# EditProfile
| Responsibilities |Collaborators|
|:-----:|:-----:|
| Displays the edit profile layout| UserProfile|
| Get user information from the firebase  database using the AAID | Firebase database|
| updates and deletes user information from the firebase database||
| Goes back to the dashboard UI when the user applies changes or cancels|| 


# Dashboard
| Responsibilities |Collaborators|
|:-----:|:-----:|
| Displays the dashboard of the UI application|Firebase database
| Handle click events for QR code scanning, user profile details, leaderboard, highest score, and scan history|Drawerbase
| Retrieve user information and data from Firestore||
| Display a small-scale map with indications of surrounding scannable code||






# UserProfile
| Responsibilities | Collaborators |
|:-----:|:-----:|
| Displays user info which includes: QR ID.|Darwebase|
| > Username, email.|user|
| > Calculates and displays.|| 
| > Highest score. Lowest score.||
| > Total score||
| > Total number of QR codes scanned||
|Allows editing of username and email.||
|Retrieve users data from firebase firestore database||
|Handles user interaction with profile editing button||

# User
|Responsibilities | Collaborators|
|:-----:|:-----:|
|Allows the setting and getting QR ID,Qrcode,Username,User email|UserProfile|
| display Highest score,Lowest score,Total score,Total number of QR codes scanned|firebase|

# DrawerBase
| Responsibilities | Collaborators|
|:-----:|:-----:|
|Sets up the base for the operation of drawing|userProfile|
|Creates a sidebar to navigate between activities|Dashboard|
|To be notified when an item in navigation view is clicked and take the corresponding action|MapsActivity|
|Set up all the titles for the class that extends DrawerBase class|ScanCode|
|ScanHistory||
|LeaderBoard||
|SearchHistory||

# QRDetailActivity
|Responsibilities |Collaborators|
|:-----:|:-----:|
|when requested from ScanCode or other Activity launch allow user change or view QR code details |QrDetailGeneralFragment||
|The three fragement fov|QrDetialImageFragment|
||QrDetailLocationFragment|
||SectionPageAdapter|
||GameQrRecordDB|
||Scancode|

# QRDetailGeneralFragment 
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Display QR code name, points and visual in the fragment if user reviewing|QrDetailActivity|
|Allow editing of QR code name by the user on the app screen|SectionPageAdapter|
|Allow deleting of the QR code by the user on the app screen when reviewing||
|Provide error checking for unique QR code name when typing||
|Display the creation date of the QR code for the user||











# QRDetailImageFragment
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Display nearby environment if user decide take a photo |QRDetailActivity|
|Notify the listener when the image enable switch is toggled|SectionPageAdapter|
|Load the image from the URI for a new QR code in the fragment|


# QRDetailLocationFragment
|Responsibilities |Collaborators|
|:-----:|:-----:|
| when adding qr code |QrDetailActivty|
| Read user location and get nearest point of interest location and display address/point name etc. | SectionPageAdapter|
| Show Mapbox map view and enable drag and select | MapboxMap|
| When review qr code||
| Fetch information from fire base ||
| If enable location show point of interest, and show map view||
| If disable show nothing and disable switched||

# ScanCode
| Responsibilities | Collaborators|
|:-----:|:-----:|
|Allows users to scan QR codes using their camera| DrawerBase|
|Decodes the QR code to obtain embedded data| Firebase ML kit(QR reconization)|
|Validate QR code to make sure its correct | Camerax|
|Returns the decoded data ||
| score calculation ||
| load image drawing||



# SectionsPagerAdapter
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Create and manage fragments for each page in the ViewPager|QRDetailLocationFragment|
|Keep count of the number of pages in ViewPager|QRDetailImageFragment|
|Return the title for each tab|QrDetailGeneralFragment|
|Passes information about the location of the QR code to activity|QrdetailActivity|
|When info updated,pass information to all fragments |

# GameQrRecordDB
| Responsibilities| Collaborators|
|:-----:|:-----:|
|Store a new QR code record (image, geolocation, score etc..)in the database|Random generate user qrname|
|Keep access record locally(geolocation, picture,etc)|Data preprocessing before upload|
|When one of the data change(eg. Add,edit or remove), update the data in DB|QRDetailActivity|
|anythingChanged notify PlyProfile,NearbyQrDB||
|Delete the image only in the database||
|Delete the location only in the database||
|Remove the entire record in the database||










# ScanHistoryCustomList
|Responsibilities|Collaborators|
|:-----:|:-----:|
|ScanHistoryQRRecord|ScanHistoryCustomList|
|Show scan history records in a customized view order by newest to oldest||
|Use a specific layout for each item in the list||
|Extract data to obtain the info on a qrcode||
|Assign extracted data to corresponding TextViews||
|Handle empty location field and display default message||

# ScanHistory
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Stores all the information for scanned qr code in one place|Drawerbase|
|Provides methods to retrieve QR name, points, date, and location from this place|firebase|
|List item in reverse time order||


# ScoreCounter
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Calculates the score of inputted QRhash|Scancode|
|Counts the number of occurrences of each character in the hash||
|Stores the calculated score||

# ScanHistory
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Displays the QR scan history for a specified user|Drawerbase|
|Allows the user to view their scan history details by retrieving it from the firebase firestore database|firebase|
|Update the scan history when changes occur to the firebase firestore database


# QRhash
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Calculate the SHA-256 hash value of an input string|scancode|
|Convert the byte array representation of the hash value to a hex string||









# MapsActivity
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Requests the users location permission if its not already enabled|Firebase|
|Display google map with the user's current location and markers|Drawerbase|
|Moves the users camera to their current position||
|Gets markers from the firebase firestore databaseAdd click listener to the marker's info window||
|Manage the click event of the information window||


# EmojiDraw
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Draw a visual representation of an emoji face with random features|Scancode|
|Generate the face structure randomly||
|Generate the size of the face randoml||
|Generate the position and style of the eyebrows,eyes,nose,mouth,facial hair randomly||
|Print the visual representation of the emoji face||

# ScanHistoryQRRecord
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Each Scan History class saving inside  the custom list|ScanHistoryCustomList|







# NearbyQrCodeList
|Responsibilities|Collaborators|
|:-----:|:-----:|
Display the surrounding qrcode list within 1 KM,connect with firebase for update
Firebase database
Maps Activity
QrCodeList



# QrCode
|Responsibilities|Collaborators|
|:-----:|:-----:|
|For nearby QRcode list ,each item is a Qrcode class,it save all the data|QrCodeList|


# QRCodeDetailFragment
|Responsibilities|Collaborators|
|:-----:|:-----:|
|When tapping on a selection of item,show more detail on a Dialog Fragment|QrCode||
||NearByQrCodeList|


# QrCodeList
|Responsibilities|Collaborators|
|:-----:|:-----:|
|Displaying each record of nearby QR into its view and display to UI|QRCode|
||NearByQrcodeLIst|



























