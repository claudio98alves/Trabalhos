import React from 'react';
import { createAppContainer } from 'react-navigation';
import { createStackNavigator } from 'react-navigation-stack';

import MenuScreen from '../screens/MenuScreen';
import JoinGameScreen from '../screens/JoinGameScreen';
import LobbyScreen from '../screens/LobbyScreen';
import HostGameScreen from '../screens/HostGameScreen';
import QRcodeScreen from '../screens/QRcodeScreen';
import QuestionsScreen from '../screens/QuestionsScreen';
import Colors from '../constants/colors'
import AddQuestionScreen from '../screens/AddQuestionScreen';
import GameScreen from '../screens/GameScreen';
import GameOverScreen from '../screens/GameOverScreen';
import HowToPlayScreen from '../screens/HowToPlayScreen';

const MenuNavigator = createStackNavigator({
    Menu: MenuScreen,
    JoinGame: JoinGameScreen,
    HostGame: HostGameScreen,
    QRcode: QRcodeScreen,
    Lobby: LobbyScreen,
    QuestionsScreen:QuestionsScreen,
    AddQuestion:AddQuestionScreen,
    GameScreen:GameScreen,
    GameOver:GameOverScreen,
    Help: HowToPlayScreen,

}, {
    defaultNavigationOptions: {
        headerStyle: {
            backgroundColor: Colors.bckcolor,
        },
        headerTitleStyle: {
            textAlign: "left",
            fontFamily: "archivo-bold",
            flex: 1,
        },
        headerTintColor: Colors.bordercolor,

    }
});


export default createAppContainer(MenuNavigator);