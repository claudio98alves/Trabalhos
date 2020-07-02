import React, { useState } from 'react';
import { View, Text, StyleSheet, Dimensions, Alert } from 'react-native';
import QRCode from 'react-native-qrcode-svg';
import DefaultStyles from '../constants/default-styles';
import Btn from '../components/Btn';
import Aspect from '../constants/aspect';

const QRcodeScreen = props => {
    const [numberPlayers, setNumberPlayers] = useState(0);
    const username = props.navigation.getParam('username');
    const numberQuestions = props.navigation.getParam('numberQuestions');
    const theme = props.navigation.getParam('theme');
    const socket = props.navigation.getParam('socket');
    const roomId = props.navigation.getParam('roomId');

    const StartGameHandler = () => {
        if (numberPlayers >= 0) {
            socket.emit('StartGame', { numberQuestions: numberQuestions, theme: theme });
            socket.on('MakeQuestions', function (data) {
                props.navigation.navigate({
                    routeName: 'QuestionsScreen',
                    params: {
                        username: username,
                        socket: socket,
                        roomId: roomId,
                        theme: theme,
                        numberQuestions: numberQuestions
                    }
                });

            });
        }else{
            Alert.alert('No players joined!', 'You need at least 1 player.', [{ text: 'Okay', style: 'destructive' }]);
        }

    }

    socket.on('playerJoined', () => { setNumberPlayers(numberPlayers + 1) })


    return (
        <View style={DefaultStyles.screen}>
            <View style={styles.textContainer}>
                <Text style={styles.plainText}>Read the following QRCode and join me at the game!</Text>
            </View>
            <QRCode size={Dimensions.get('window').width * 0.65} value={String(roomId)} />
            <Text style={DefaultStyles.title}>People Joined:</Text>
            <Text style={styles.numberText}>{numberPlayers}</Text>
            <Btn style={{ marginTop: Dimensions.get("window").height * Aspect.marginValue }}
                onPress={StartGameHandler}>Start Game</Btn>
        </View>
    );
};

QRcodeScreen.navigationOptions = {
    headerTitle: "Lobby",
};

const styles = StyleSheet.create({
    numberText: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.06,
        color: "white",
        marginTop: Dimensions.get("window").height * Aspect.marginValue,
    },
    textContainer: {
        fontFamily: 'archivo-regular',
        marginBottom: Dimensions.get("window").height * Aspect.marginValue,
        width: Dimensions.get('window').width * 0.65,
    },
    plainText: {
        fontFamily: 'archivo-regular',
        color: 'white',
        fontSize: Dimensions.get('window').height * 0.025
    }
});

export default QRcodeScreen;