import React, { useState } from 'react';
import { Alert, Picker, View, Text, StyleSheet, Button,Dimensions, Keyboard, TouchableWithoutFeedback } from 'react-native';
import Input from '../components/Input';
import io from 'socket.io-client';
import Colors from '../constants/colors';
import Aspect from '../constants/aspect';
import DefaultStyles from '../constants/default-styles';
import Btn from '../components/Btn';
import {SocketOptions, ipAddr} from '../data/socketOptions';

/*<Text style={DefaultStyles.title}>Choose the theme:</Text>
                <View style={styles.pickerContainer}>
                    <Picker selectedValue={theme}
                        onValueChange={(itemValue, itemIndex) => { themeInputHandler(itemValue) }}
                        style={styles.itemContainer}
                        itemStyle={styles.item}
                    >
                        <Picker.Item label="Random" value="Random" />
                        <Picker.Item label="Tecnologia" value="Tecnologia" />
                        <Picker.Item label="Arte" value="Arte" />
                        <Picker.Item label="Entretenimento" value="Entretenimento" />
                        <Picker.Item label="Ciências" value="Ciências" />
                    </Picker>
                </View>
*/


const HostGameScreen = props => {
    const [username, setUsername] = useState('');
    const [numberQuestions, setNumberQuestions] = useState('');
    const [theme, setTheme] = useState('Random');


    const usernameInputHandler = (inputText) => {
        setUsername(inputText);
    }

    const numberInputHandler = (inputText) => {
        setNumberQuestions(inputText.replace(/[^0-9]/g, ''));
    }

    const themeInputHandler = (itemValue) => {
        setTheme(itemValue);
    }

    const confirmInputHandler = () => {
        if (!username || username.replace(/\s+/g, '').length === 0) {
            Alert.alert('Invalid Username!', 'Username cannot be empty', [{ text: 'Okay', style: 'destructive' }]);
            return;
        }
        const chosenNumber = parseInt(numberQuestions);
        if (isNaN(chosenNumber) || chosenNumber <= 0) {
            Alert.alert('Invalid number of questions!', 'Number has to be greater than 0', [{ text: 'Okay', style: 'destructive' }]);
            return;
        }

        var socket = io(ipAddr,SocketOptions);
        socket.on('validation', function (data) {
            socket.emit('host', { username: username, numberQuestions: numberQuestions });
            socket.on('roomId', function (data) {
                props.navigation.navigate({
                    routeName: 'QRcode',
                    params: {
                        numberQuestions: numberQuestions,
                        username: username,
                        theme: theme,
                        socket: socket,
                        roomId:data.roomId
                    }
                });
            })
        });
    }

    return (
        <TouchableWithoutFeedback onPress={() => { Keyboard.dismiss() }}>
            <View style={DefaultStyles.screen}>
                <Text style={DefaultStyles.title}>Username:</Text>
                <Input
                    blurOnSubmit
                    autoCapitalize='none'
                    autoCorrect={false}
                    keyboardType='default'
                    onChangeText={usernameInputHandler}
                    value={username}
                />
                <Text style={DefaultStyles.title}>Number of questions:</Text>
                <Input style={styles.questionContainer}
                    blurOnSubmit
                    autoCapitalize='none'
                    autoCorrect={false}
                    maxLength={2}
                    keyboardType='number-pad'
                    onChangeText={numberInputHandler}
                    value={numberQuestions}
                    
                />
                
                <Btn style={{ marginTop: Dimensions.get("window").height * 0.086 }} onPress={confirmInputHandler}>Create Room</Btn>
            </View>

        </TouchableWithoutFeedback>
    );

};

HostGameScreen.navigationOptions = {
    headerTitle: "Host Game",
};
const styles = StyleSheet.create({
    questionContainer: {
        textAlign: 'center',
        width: Dimensions.get("window").width * 0.15,
        paddingLeft: 0,
        paddingHorizontal: 0
    },
    pickerContainer: {
        width: Dimensions.get("window").width * 0.65,
        height: Dimensions.get("window").height * 0.07,
        borderRadius: 12,
        borderWidth: 3,
        borderColor: Colors.bordercolor,
        marginTop: Dimensions.get("window").height * Aspect.marginValue,
        backgroundColor: "white"
    },
    itemContainer: {
        width: '100%',
        height: '100%',
    },
    item: {
        textAlign: "center",
    }
});

export default HostGameScreen;
