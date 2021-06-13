<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
***
***
***
*** To avoid retyping too much info. Do a search and replace for the following:
*** ebaloaloa, lick-hunter-server, twitter_handle, email, Lick Hunter Server, Backend Service for Lick Hunter Web Application
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/ebaloaloa/lick-hunter-server">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Lick Hunter Server</h3>

  <p align="center">
    Backend service for Lick Hunter PRO Web Application
    <br />
    <a href="https://github.com/ebaloaloa/lick-hunter-server"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/ebaloaloa/lick-hunter-server">View Demo</a>
    ·
    <a href="https://github.com/ebaloaloa/lick-hunter-server/issues">Report Bug</a>
    ·
    <a href="https://github.com/ebaloaloa/lick-hunter-server/issues">Request Feature</a>
  </p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
        <li><a href="#telegram">Setup Telegram Bot</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
    <li><a href="#donations">Donations</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](http://www.lickhunter.com/)

This is the backend service for the Lick Hunter Web Application.  
Front-end development is in progress. [front-end repo](https://github.com/ebaloaloa/lick-hunter-admin)  

[Click here to view Lick Hunter website](http://www.lickhunter.com/) 

### Built With

* [Maven](https://maven.apache.org/)


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

1. Install [Maven](https://maven.apache.org/)
2. Install [JDK](https://www.oracle.com/sg/java/technologies/javase-downloads.html)
3. Set JAVA_HOME environment variable to JDK installation folder

### Build

1. Clone the repo
   ```sh
   git clone https://github.com/ebaloaloa/lick-hunter-server.git
   ```
2. Maven Build
   ```sh
   mvn clean install -DskipTests
   ```
   
### Installation

1. Unzip server-0.0.1-SNAPSHOT-distribution.zip from the ``server/target`` directory
2. Copy the contents of server-0.0.1-SNAPSHOT directory to root directory of LickHunterPRO
3. Execute command. This will generate ``coins.json``
   ```sh
   java -XX:+UseParallelGC -Xms512m -Xmx2000m -jar server-0.0.1-SNAPSHOT.jar
   ```
   OR
   ```java 
   sh start.sh
   ```  

### Telegram  

1. Search for @BotFather in Telegram to create a new bot.  
2. Type command ``/newbot`` to create a new bot.  
3. Choose a name for your new bot.  
4. Choose a username for your new bot.  
6. After you have successfully created a new bot, go to ```/mybots``` to edit your newly created bot.  
7. Select the new bot from the list.  
8. Select API Token then copy/paste the token to ``telegram.token`` inside the application.properties.  
9. Return to previous menu > Edit Bot > Edit Commands.  
   Copy/Paste the following to create new commands:  
   ```
   balance - Check balance  
   disablerestart - Disables scheduled restart of lickhunter  
   enablerestart - Enables scheduled restart of lickhunter  
   startprofit - Start Lickhunter Profit
   stopprofit - Stop Lickhunter Profit
   startwebsocket - Start Lickhunter Websocket
   stopwebsocket - Stop Lickhunter Websocket  
   settings - Change active settings from user defined settings  
   pause - Pause bot after all positions are closed  
   resume - Resume bot  
   ```  
10. Open application.properties and change the following values:  
* **telegram.bot** - Name of the bot you created   
* **telegram.username** - List of allowed telegram usernames to have access on your bot. Bots created in Telegram are public, this is to ensure that only you can see your commands.  
* **telegram.enable** - Set to ``true`` to enable the bot. Otherwise, telegram bot will not run.  

<!-- USAGE EXAMPLES -->  
## Usage  
  
###query.json  
````
{
  "symbol": null,       
  "maxPriceChangePercent":null,
  "volumeUpperLimit":3000000000,
  "volumeLowerLimit":0,
  "minimumTradingAge":30,
  "percentageFromAllTimeHigh":10,
  "exclude":["DOGE","BTC","ETH","XRP"],
  "autoExclude" : true,
  "autoExcludePercentage" : 70
}
````
* **symbol:** Specifies the symbol pair to search. Example: ``BTCUSDT``  
* **maxPriceChangePercentage:** Identifies the maximum allowed percentage change(-/+) for the past 24H.  
* **volumeUpperLimit:** Maximum volume limit for the past 24H
* **volumeLowerLimit:** Minimum volume limit within 24H
* **minimumTradingAge:** Minimum trading age required for coins
* **percentageFromAllTimeHigh:** Coins will not be traded when this threshold(+/-)   is reached  
* **exclude:** Coins in this list will not be included in coins.json  
* **autoExclude:** Enables automatic exclusion of coins based on percentage change within 24H.  
* **autoExcludePercentage:** Sets the percentage change for automatic exclusion.  

###web-settings.json  
````
{
  "active":"settings1",
  "defaultSettings":"settings1",
  "safe":"settings2",
  "userDefinedSettings":{
    "settings1":{
      "maxOpen":10,
      "openOrderIsolationPercentage":5,
      "longOffset":3,
      "shortOffset":3,
      "lickValue":1000,
      "marginPercentNotification":30,
      "marginType":"crossed",
      "leverage":4,
      "autoLickValue":true
    },
    "settings2":{
      "maxOpen":5,
      "openOrderIsolationPercentage":3,
      "longOffset":10,
      "shortOffset":3,
      "lickValue":1000,
      "marginPercentNotification":30,
      "marginType":"crossed",
      "leverage":4,
      "autoLickValue":true
    }
  }
}
````  
* **active:** Identifies the current active settings from the user-defined settings.  
* **defaultSettings:** Identifies the default settings from the user-defined settings. This will be active if volatility of bitcoin is less than 
  the specified volatility amount.  
* **safe:** Identifies the safe settings from the user-defined settings. Safe settings will be active if bitcoin
  reaches certain volatility. Volatility Indicator can be found here https://lunarcrush.com/developers/tradingview.
  See application.properties to change volatility settings.  
* **userDefinedSettings:** Users can create multiple settings. Settings in ``active`` property will be used. Users
  can also change settings through Telegram bot. See application.properties to setup Telegram Bot.  
* **maxOpen:** Maximum allowed positions at a time
* **openOrderIsolationPercentage:** New positions will not be opened when percentage of total balance is reached
* **marginPercentNotification:** Discord notification when margin reached a percentage of total balance  
* **autoLickValue:** Enables retrieving of liquidation value from liquidation.wtf  

###Sample API Requests (WIP will update soon)  
**Save income history into database**   
```/api/application/income_history?limit=10&incomeType=REALIZED_PNL```     

**Save candlestick data into database**  
``/api/application/candlestick?interval=DAILY&limit=500``

**Subscribe to candlestick data**  
``/api/application/candlestick/subscribe``  

**Subscribe to user data**  
``/api/application/userdata``  

**Subscribe to mark price data**  
``/api/application/markprice``  

**Subscribe to mark price data**  
``/api/application/markprice``  

**Save account information into database**  
``/api/application/account_information``  

**Save income history into database**  
````
http://localhost:8081/api/application/income_history
http://localhost:8081/api/application/income_history?limit=1000&incomeType=REALIZED_PNL
````   

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/ebaloaloa/lick-hunter-server/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<!-- CONTACT -->
## Contact

Klenk - [Ephraim Baloaloa](https://www.linkedin.com/in/ephraimbaloaloa/) - ebaloaloa@gmail.com

Project Link: [https://github.com/ebaloaloa/lick-hunter-server](https://github.com/ebaloaloa/lick-hunter-server)  
Project Link: [https://github.com/ebaloaloa/lick-hunter-admin](https://github.com/ebaloaloa/lick-hunter-admin)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* [Daisy's LHPControl](https://github.com/daisy613/LHPControl)
* [LickHunterPRO](https://github.com/CryptoGnome/LickHunterPRO)
* [doosjenever](https://github.com/doosjenever/Scripts)

<!-- DONATIONS -->
## Donations

* USDT (ERC20): 0xe1eb10322d33f768c29b3c56e16beca6e1b888a7
* USDT (TRC20): TF6zmQxCP5hT4f9r4nLe1pYaC3daEmAVuQ
* BTC: 1N7gtMygqxKMzo1jhzBdG7WnbNpoCB87pa






<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/ebaloaloa/lick-hunter-server.svg?style=for-the-badge
[contributors-url]: https://github.com/ebaloaloa/lick-hunter-server/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ebaloaloa/lick-hunter-server.svg?style=for-the-badge
[forks-url]: https://github.com/ebaloaloa/lick-hunter-server/network/members
[stars-shield]: https://img.shields.io/github/stars/ebaloaloa/lick-hunter-server.svg?style=for-the-badge
[stars-url]: https://github.com/ebaloaloa/lick-hunter-server/stargazers
[issues-shield]: https://img.shields.io/github/issues/ebaloaloa/lick-hunter-server.svg?style=for-the-badge
[issues-url]: https://github.com/ebaloaloa/lick-hunter-server/issues
[license-shield]: https://img.shields.io/github/license/ebaloaloa/lick-hunter-server.svg?style=for-the-badge
[license-url]: https://github.com/ebaloaloa/lick-hunter-server/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/ephraimbaloaloa/