#  PlayerProfile

## PlayerProfileVaildate

| Responsibilities                     | Collaborators             |
| :----------------------------------- | :------------------------ |
| Options to validate if user first time using the app | PlayerProfileDBController |
| Connect with cloud db for verify     |                           |

## PlayerProfileRegister

| Responsibilities                                    | Collaborators             |
| :-------------------------------------------------- | :------------------------ |
| Record the device_id for future reconization        | PlayerProfileDBController |
| Generate qr code profile for new user               |                           |
| Ability to remember the users device                |                           |

## PlayerProfileModification

| Responsibilities                        | Collaborators             |
| :-------------------------------------- | :------------------------ |
| Allow user to modify their profile      | PlayerProfileDBController |
| Delete account                          |                           |

## PlayerProfileRemove

| Responsibilities                                 | Collaborators             |
| :----------------------------------------------- | :------------------------ |
| Allow user to delete all the content in database | PlayerProfileDBController |

## PlayerProfileSearch

| Responsibilities                           | Collaborators             |
| :----------------------------------------- | :------------------------ |
| Find others users and access their profile | PlayerProfileDBController |

## PlayerProfileLeaderBoard

| Responsibilities        | Collaborators             |
| :--------------------   | :------------------------ |
| Show top score boards   | PlayerProfileDBController |
| allow user choose region| |

## Logoned mainActivity

| Responsibilities                                  | Collaborators      |
| :------------------------------------------------ | :----------------- |
| Allow user to add new qr codes                    | PlayerProfileDBconnect      |
| Allow user access their QRrecord                  | GameQrAddActivity  |
| Go to see  all scanned information                | GameQrCodeActivity |
| See highest score/lowest score/total score directly | GameQRcode         |
| Allow user go to scanned location map             | MapActivity        |

## PlayerProfileDBController

| Responsibilities            | Collaborators |
| :-------------------------- | :------------ |
| Store new user information  | PlayerProfileDBconnect |
| Update modified information | GameQRcode    |
| Delete  user information    |               |

## PlayerProfileDB

| Responsibilities                             | Collaborators |
| :------------------------------------------- | :------------ |
| Store new user information                   |  PlayerProfileDBconnect |
| Update modified information                  | GameQRcode    |
| Delete  user information                     |               |
| Saving all the account holding information   |               |
| Save the token/ two step verification codes  |               |
| Privacy preferences(eg. No storage with IDs) |               |

## PlayerProfileDBConnect

| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
| Connects to the Firebase Firestore database | PlayerProfileDB |

# GameQrCode

## GameQrCodeActivity

| Responsibilities                            | Collaborators                 |
| ------------------------------------------- | ----------------------------- |
| Display the information to the screen       | GameQrRecordStorageController |
| Accept input from the user for modification | QrCodeActivity                |
| Accept input from the user for deletion     |                               |
| Allow user return to the main account page  |                               |

## GameQRSummaryActivity
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Showing highest score|GameQrRecordStorageController|
|Showing lowing score|Logoned main activity|
|Showing total score||

## GameQRSum
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|shows the sum of all QR code|GameQRRecordStorage|
||PlayerProfile|


## GameQRAddActivity
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
| Adding QR record into the DB|GameQrRecordStorageController|

## GameQRChangeActivity
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
| Give users can update if they want to remove their information.|GameQrRecordStorageController|

## GameQRRecordStorageController
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Keep access record locally(geolocation, picture,etc)|NearbyQRController|
|When one of the data change(eg. Add,edit or remove), update the data in DB|PlayerProfile|
|anythingChanged notify PlyProfile,NearbyQrDB|

## GameQrRecordDB
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Store a new QR code record (image, geolocation, score etc..)in the database| UserDBconnect |
|Delete the image only in the database |QrRecordDBconnect|
|Delete the location only in the database||
|Remove the entire record in the database||


## GameQrRecordDBconnect
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Connect to QR record Firestore database | QrRecordDB|

## GameQRScanner
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Record geolocation|GameQrRecordStorage|
|Scan QR code for said location||

## GameQrProcessing
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Validate the QR code and if valid would be stored in the database|  QrRecordDBconnect|
|If invalid it would return a error message||

## GameQrDrawing
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Using dictionaries,draws the Qr monsters| QrRecordDBconnect|
|Calculates the score of a QR code when given|QrRecordDB|


# NearbyQR
## ShowingNearbyQR
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Show all the nearbyQR in a map|QrRecordDBconnect|
|Able to click to see more details such as image|QrRecord|

## RemovingNearbyQR
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Delete record in DB|QrRecordDBconnect|

## AddingNearbyQR
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Save GameQR location and point to DB|QrRecordDBconnect|


## NearbyQRController
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|When an new/remove requested, adding a  record in a hashed DB|UserDBconnect
|Store the data divided by region(could be split continent,  country,  attitude etc.)|QrRecordDBconnect|


## NearbyQrDBConnect
| Responsibilities                            | Collaborators   |
| :------------------------------------------ | :-------------- |
|Connect to NearbyQR record Firestore database|NearybyQrRecordDB|








