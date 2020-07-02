import React from 'react';
import { View, Text, Button, StyleSheet, Dimensions, Image, StatusBar } from 'react-native';
import Btn from '../components/Btn';
import Colors from '../constants/colors';
import DefaultStyles from '../constants/default-styles';
import Logo from '../assets/method-draw-image.svg';

const MenuScreen = props => {
    return (
        <View style={styles.screen}>
            <StatusBar
                hidden={true}
            />
            <View>
                <Logo width={300} height={200} />
            </View>
            <View style={styles.buttonContainer}>
                <Btn onPress={() => {props.navigation.navigate({ routeName: 'HostGame' })}}>Host Game</Btn>
                <Btn onPress={() => { props.navigation.navigate({ routeName: 'JoinGame' }) }}>Join Game</Btn>
                <Btn style={styles.btn} styleText={styles.btnText} onPress={() => { props.navigation.navigate({ routeName: 'Help' }) }}>How to Play</Btn>
            </View>
        </View>);
};
MenuScreen.navigationOptions = {
    headerShown: false,
};
const styles = StyleSheet.create({
    screen: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: Colors.bckcolor,
        paddingTop: 50

    },
    btnText: {
        fontSize: Dimensions.get('window').width * 0.5 * 0.12,
        fontFamily: 'archivo-regular'
    },
    btn: {
        width: Dimensions.get('window').width * 0.5,
        height: Dimensions.get('window').height * 0.05
    },
    buttonContainer: {
        flex: 1,
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "space-around",
        maxHeight: "40%"
    },
    text: {
        fontFamily: 'archivo-bold',
        fontSize: 50,
        marginBottom: 70,
        marginTop: 50
    }
});

export default MenuScreen;
