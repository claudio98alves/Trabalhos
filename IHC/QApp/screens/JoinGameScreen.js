import React, { useState } from 'react';
import { View, Text, Button, StyleSheet, Alert, Dimensions, TouchableWithoutFeedback, Keyboard, Platform } from 'react-native';
import { BarCodeScanner } from 'expo-barcode-scanner';
import * as Permissions from 'expo-permissions';
import Input from '../components/Input';
import io from 'socket.io-client';
import DefaultStyles from '../constants/default-styles';
import Btn from '../components/Btn';
import {SocketOptions, ipAddr} from '../data/socketOptions';

const JoinGameScreen = props => {
    const [permission, setPermission] = useState(false);
    const [username, setUsername] = useState('');
    const [scan, setScan] = useState(false);

    const getPermissionsAsync = async () => {
        const result = await Permissions.askAsync(Permissions.CAMERA);
        if (result.status !== 'granted')
            Alert.alert('Insufficient permissions!', 'You need to grant camera permissions to scan the QRcode', [{ text: 'Okay' }]);
        else {
            if (!username || username.replace(/\s+/g, '').length === 0) {
                Alert.alert('Invalid Username!', 'Username cannot be empty', [{ text: 'Okay', style: 'destructive' }]);
                return;
            }
            setScan(true);
            props.navigation.setParams({'isCamera': false})
        }
    };

    const usernameInputHandler = (inputText) => {
        setUsername(inputText);
    }
    const scanButtonHandler = () => {
        getPermissionsAsync();
    }

    const cancelButtonHandler = () => {
        props.navigation.setParams({'isCamera': true})
        setScan(false);
    }

    const handleBarCodeScanned = ({ type, data }) => {
        setScan(false);
        var roomId = data;

        var socket = io(ipAddr,SocketOptions);
        socket.on('validation', function (data) {
            socket.emit('join', { username: username, roomId: roomId });
            socket.on('joinResult', function (data) {
                if (data.bool) {
                    props.navigation.navigate({
                        routeName: 'Lobby',
                        params: {
                            username: username,
                            socket: socket,
                            roomId: roomId
                        }
                    });
                } else {
                    Alert.alert('Username already taken!', 'You need to choose another username.', [{ text: 'Okay' }])
                }

            });
        });
    };
    if (scan) {
        return (
            <View style={styles.screen}>
                <BarCodeScanner
                    onBarCodeScanned={scan ? handleBarCodeScanned : undefined}
                    style={StyleSheet.absoluteFillObject}>
                </BarCodeScanner>
                <View style={styles.topContainer}></View>
                <View style={styles.midContainer}>
                    <View style={styles.leftContainer}></View>
                    <View style={styles.rightContainer}></View>
                </View>
                <View style={styles.bottomContainer}>
                    <View style={styles.btnContainer}>
                        <Button color={Platform.OS === 'android' ? 'transparent' : 'white'} title="Cancel" onPress={cancelButtonHandler} />
                    </View>
                </View>
            </View>
        );
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
                <Btn style={styles.button} styleText={{ fontSize: Dimensions.get('window').height * 0.07 * 0.50 }}
                    onPress={scanButtonHandler}>Tap to Scan</Btn>
            </View>
        </TouchableWithoutFeedback>
    );
}

JoinGameScreen.navigationOptions = (navigationData) => {
    const isCamera= navigationData.navigation.getParam('isCamera')
    return({
        headerTitle: "Join Game",
        headerShown: isCamera
    });
};

const opacity = 'rgba(0, 0, 0, .6)';
const styles = StyleSheet.create({
    screen: {
        flex: 1,
        alignItems: 'center',
        flexDirection: 'column'
    },
    button: {
        width: Dimensions.get('window').width * 0.4,
        height: Dimensions.get('window').height * 0.07,
        marginTop: 10
    },
    btnContainer: {
        width: Dimensions.get('window').width * 0.4,
    },
    topContainer: {
        width: '100%',
        height: (Dimensions.get("window").height - Dimensions.get('window').width * 0.65) / 2,
        backgroundColor: opacity
    },
    midContainer: {
        width: '100%',
        height: Dimensions.get('window').width * 0.65,
        flexDirection: 'row',
        justifyContent: 'space-between'
    },
    bottomContainer: {
        width: '100%',
        height: (Dimensions.get("window").height - Dimensions.get('window').width * 0.65) / 2,
        backgroundColor: opacity,
        alignContent: 'center',
        justifyContent: 'center',
        alignItems: 'center'

    },
    leftContainer: {
        width: (Dimensions.get("window").width - Dimensions.get('window').width * 0.65) / 2,
        backgroundColor: opacity,
    },
    rightContainer: {
        width: (Dimensions.get("window").width - Dimensions.get('window').width * 0.65) / 2,
        backgroundColor: opacity,
    }

});

export default JoinGameScreen;