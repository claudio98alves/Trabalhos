import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import Colors from '../constants/colors';
const QuestionItem = props => {
    return (
        <TouchableOpacity onPress={props.onSelectQuestion}>
            <View style={styles.questionItem}>
                <Text style={styles.questionText}>Q. {props.question.question}</Text>
                <Text style={styles.answerText}>A1. {props.question.answers[0].answer}</Text>
                <Text style={styles.answerText}>A2. {props.question.answers[1].answer}</Text>
                <Text style={styles.answerText}>A3. {props.question.answers[2].answer}</Text>
                <Text style={styles.answerText}>A4. {props.question.answers[3].answer}</Text>
            </View>
        </TouchableOpacity>
    );
}

const styles = StyleSheet.create({
    questionItem: {
        width: '95%',
        backgroundColor: Colors.btncolor,
        borderRadius:20,
        overflow:'hidden',
        padding:8,
        marginVertical:10,
        alignSelf: 'center'
    },
    questionText:{
        color: 'white',
        fontFamily: 'archivo-bold',
        fontSize: Dimensions.get('window').height * 0.025
    },
    answerText:{
        color: 'white',
        fontFamily: 'archivo-regular',
        fontSize: Dimensions.get('window').height * 0.025
    }
});

export default QuestionItem;