import React from 'react';
import { View, Text, StyleSheet, ActivityIndicator, Dimensions, Platform} from 'react-native';
import DefaultStyles from '../constants/default-styles';
import Aspect from '../constants/aspect';
import Colors from '../constants/colors';

const LobbyScreen = props => {
    
    const username = props.navigation.getParam('username');
    const socket = props.navigation.getParam('socket');
    const roomId=props.navigation.getParam('roomId');
    
    socket.on('MakeQuestions',function (data){
        props.navigation.navigate({
            routeName: 'QuestionsScreen',
            params: {
                username: username,
                socket: socket,
                roomId:roomId,
                theme:data.theme,
                numberQuestions:data.numberQuestions
            }
        });

    })
    return (
        <View style={styles.screen}>
            <ActivityIndicator size={Platform.OS === 'android' ? Dimensions.get("window").height * 0.24 : 'large'}/>
            <Text style={DefaultStyles.title}>Wait for everyone!</Text>
        </View>);
};

LobbyScreen.navigationOptions = {
    headerShown: false,
};

const styles = StyleSheet.create({
    screen:{
        flex: 1,
        alignItems: 'center',
        backgroundColor: Colors.bckcolor,
        paddingTop: Dimensions.get("window").height * Aspect.marginValue,
        paddingBottom: Dimensions.get("window").height * Aspect.marginValue,
        justifyContent: 'center'
    }
});

export default LobbyScreen;