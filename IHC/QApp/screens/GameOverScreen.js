import React from 'react';
import { StyleSheet, View, Text, Dimensions } from 'react-native';
import Btn from '../components/Btn';
import Colors from '../constants/colors';
import {useDispatch} from 'react-redux';
import Medalha from '../assets/medalha.svg';
import Cemiterio from '../assets/cemiterio.svg';
import { NavigationActions } from 'react-navigation';
import {resetState} from '../store/actions/game';


const GameOverScreen = props => {
    const socket = props.navigation.getParam('socket');
    const roomId = props.navigation.getParam('roomId');
    const top3 = props.navigation.getParam('top3');
    const place = props.navigation.getParam('place');
    const points = props.navigation.getParam('points');
    
    const dispacth = useDispatch();

    const returnMainMenu = () => {
        dispacth(resetState())
        socket.emit('disconect', { roomId: roomId });
        props.navigation.reset([NavigationActions.navigate({ routeName: 'Menu' })], 0);
    }

    return (
        <View style={styles.screen}>
            <View style={styles.topContainer}>
                <View style={styles.toptopContainer}>
                    <Text style={styles.text4}>You got,</Text>
                    {place == 1 && <Text style={styles.text5}>1st place!</Text>}
                    {place == 2 && <Text style={styles.text5}>2nd place!</Text>}
                    {place == 3 && <Text style={styles.text5}>3rd place!</Text>}
                    {place > 3 && <Text style={styles.text5}>{place}th place!</Text>}
                </View>
                <View style={styles.bottopContainer}>
                    {place == 1 ? <Medalha width={50} height={50} /> : <Cemiterio width={50} height={50} />}
                </View>
            </View>
            <View style={styles.midContainer}>
                <Text style={styles.text3}>{points} points</Text>
                <Text style={styles.text2}>Scores:</Text>
                <Text style={styles.text1}>{top3[0].username}: {top3[0].points} points</Text>
                {top3.length > 1 && <Text style={styles.text1}>{top3[1].username}: {top3[1].points} points</Text>}
                {top3.length > 2 && <Text style={styles.text1}>{top3[2].username}: {top3[2].points} points</Text>}
            </View>
            <View style={styles.botContainer}>
                <Btn onPress={returnMainMenu}>Main Menu</Btn>
            </View>
        </View>
    );
};

GameOverScreen.navigationOptions = {
    headerShown: false,
}

const styles = StyleSheet.create({
    screen: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        paddingTop: 40,
        backgroundColor: Colors.bckcolor
    },
    toptopContainer: {
        flex: 1
    },
    bottopContainer: {
        flex: 1,
        alignItems: 'center'
    },
    topContainer: {
        flex: 1,
    },
    midContainer: {
        flex: 1.5,
        backgroundColor: Colors.btncolor,
        alignItems: 'center',
        padding: 20,
        width: '100%',
        justifyContent: 'space-around'
    },
    botContainer: {
        flex: 1,
        justifyContent: 'center'
    },
    text1: {
        fontFamily: 'archivo-regular',
        fontSize: Dimensions.get("window").height * 0.03,
        color: 'white',
        textAlign: 'center'
    },
    text2: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.04,
        color: 'white',
        textAlign: 'left',
        width: '70%'
    },
    text3: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.09,
        color: 'white',
        textAlign: 'center'
    },
    text4: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.04,
        color: 'white',
        textAlign: 'center'
    },
    text5: {
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get("window").height * 0.05,
        color: 'white',
        textAlign: 'center'
    }

});

export default GameOverScreen;