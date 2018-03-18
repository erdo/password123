# Password123
[playstore listing](https://play.google.com/store/apps/details?id=co.early.password123) \| [source code](https://github.com/erdo/password123)

![image](https://raw.githubusercontent.com/erdo/password123/master/screenshot_phone_portrait.png)

Simple android Kotlin app to check passwords. It's built using the [ASAF](https://erdo.github.io/asaf-project/) framework. There is a Kotlin version (in master) and a Java version in the java branch.

This app is basically an example of how to use the [pwned4android](https://github.com/erdo/pwned4android) library. The pwned4android library wraps the "Have I been pwned" password checker service so that you can easily use it inside an android app, please see that repo for a fuller description of how it works.

### Credits

The app makes use of these third party libraries and services:

   - "Have I been pwned?", see: [https://haveibeenpwned.com/API/v2#PwnedPasswords](https://haveibeenpwned.com/API/v2#PwnedPasswords)
   
   - pwned4android, see: [https://github.com/erdo/pwned4android](https://github.com/erdo/pwned4android)
   
   - ASAF, see: [https://github.com/erdo/asaf-project](https://github.com/erdo/asaf-project)
   
   - OkHttp, see: [https://github.com/square/okhttp](https://github.com/square/okhttp)
   
   - icons from: [https://material.io/icons/](https://material.io/icons/)



### Kotlin branch

http://cloc.sourceforge.net v 1.62  T=0.47 s (100.8 files/s, 5335.1 lines/s)

| Language  | files  | blank  | comment | code |
|:----------|-------:|-------:|--------:|-----:|
| Kotlin    | 27     | 451    | 93      | 1226 |
| XML       | 20     |  97    | 3       | 618  |
| SUM:      | 47     | 548    | 96      | 1844 |



### Java branch

http://cloc.sourceforge.net v 1.62  T=0.59 s (79.4 files/s, 6306.2 lines/s)

| Language  | files  | blank  | comment | code |
|:----------|-------:|-------:|--------:|-----:|
| Java      | 27     | 537    | 89      | 2391 |
| XML       | 20     |  97    | 3       | 618  |
| SUM:      | 47     | 634    | 92      | 3009 |



## License for data

    The password data that enables this app is provided (and paid for) by "Have I been pwned"
    (haveibeenpwned.com). Access to that data comes with its own licencing terms.

	If you intend to modify this application in any way, please check
	https://haveibeenpwned.com/API/v2#License first to ensure the license supports what
	you want to do. At the time of writing, the licencing
	terms were Creative Commons Attribution 4.0 International, but
	don't take my word for it.

## License for application


    Copyright 2017-2018 early.co

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
