import React from 'react';
import {View,Text, StyleSheet, Dimensions} from 'react-native';
import DefaultStyles from '../constants/default-styles';

const HowToPlayScreen = props => {
    return(
        <View style={DefaultStyles.screen}>
            <View style={{padding: 15,paddingVertical: 30}}>
                <Text style={styles.text}>To start a game you have two options, Host or Join.</Text>
                <Text style={styles.text}>If you choose to host, you will need to choose the number of questions and then a QR code
                    will be generated, then your friends can join you !</Text>
                <Text style={styles.text}>If you choose to join, you only need to scan the QR code of your friend and you will be automatically in the room !</Text>
        
            </View>
        </View>
    );
};

HowToPlayScreen.navigationOptions = {
    headerTitle: 'How To Play'
};

const styles = StyleSheet.create({
    text:{
        color: 'white',
        fontFamily: 'archivo-regular',
        fontSize: 24,
        textAlign: "justify",
        marginVertical: 15
    }
});

export default HowToPlayScreen;