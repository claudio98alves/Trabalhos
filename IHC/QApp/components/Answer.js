import React from 'react';
import { StyleSheet, TouchableOpacity, Text, Dimensions, View } from 'react-native';
import Colors from '../constants/colors';

const Answer = props => {
    return (
        <TouchableOpacity activeOpacity={props.animation ? 0.2:1} style={[styles.answers, props.bool ? { borderWidth: 2, borderLeftWidth:0,borderColor: props.answer.bool ? 'green' : 'red' } : {}]} onPress={props.answerSelected}>
            <Text style={styles.text}>{props.answer.answer}</Text>
        </TouchableOpacity>
    );

};

const styles = StyleSheet.create({
    answers: {
        backgroundColor: Colors.btncolor,
        borderBottomRightRadius: 20,
        borderTopRightRadius: 20,
        width: '85%',
        height: '15%',
        justifyContent: 'center',
        paddingHorizontal: 20,

    },
    text: {
        color: 'white',
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get('window').height * 0.03,
        textAlign: 'left'
    }

});

export default Answer;