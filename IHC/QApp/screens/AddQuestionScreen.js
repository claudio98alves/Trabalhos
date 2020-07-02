import React, { useState } from 'react';
import { View, Text, StyleSheet, Dimensions, TouchableWithoutFeedback, Keyboard, Alert, KeyboardAvoidingView } from 'react-native';
import Btn from '../components/Btn';
import QUESTIONS from '../data/questionsData';
import Input from '../components/Input';
import AnswerInput from '../components/AnswerInput';
import Answer from '../models/answer'
import { useDispatch } from 'react-redux';
import { addQuestion, editQuestion } from '../store/actions/game';
import Question from '../models/question';
import DefaultStyles from '../constants/default-styles';

const AddQuestionScreen = props => {
    const [questionHeader, setQuestionHeader] = useState(props.navigation.getParam('questionHeader'));
    const [arrayAnswers, setArrayAnswers] = useState(props.navigation.getParam('arrayAnswers'));
    const [saved, setSaved] = useState(props.navigation.getParam('saved'));
    const [numCorrectAnswers, setNumCorrectAnswers] = useState(0);

    const index = props.navigation.getParam('index');

    const dispacth = useDispatch();

    const questionHeaderInputHandler = (inputText) => {
        setQuestionHeader(inputText);
    }

    const clearButtonHandler = () => {
        setArrayAnswers([new Answer("", false), new Answer("", false), new Answer("", false), new Answer("", false)]);
        setQuestionHeader("");
        setNumCorrectAnswers(0);
    };

    const answerInputHandler = (inputText, index) => {
        const newArray = [...arrayAnswers];
        newArray[index].answer = inputText;
        setArrayAnswers(newArray);
    }

    const selectedAnswerHandler = (index) => {
        console.log("Selected" + index);
        const newArray = [...arrayAnswers];
        newArray[index].bool = !newArray[index].bool;
        if (newArray[index].bool)
            setNumCorrectAnswers(numCorrectAnswers + 1);
        else
            setNumCorrectAnswers(numCorrectAnswers - 1);
        console.log(newArray[index].bool);
        setArrayAnswers(newArray);
    }

    const saveButtonHandler = () => {
        //Verificar se existe pelo menos 1 resposta correta
        if (arrayAnswers.findIndex(answer => answer.bool) != -1) {
            if (!saved) { // ADD
                var newQuestion = new Question(questionHeader, arrayAnswers);
                dispacth(addQuestion(newQuestion))
                props.navigation.goBack();
                setSaved(true);
            } else { //Edit 
                var newQuestion = new Question(questionHeader, arrayAnswers);
                dispacth(editQuestion(newQuestion, index))
                props.navigation.goBack();
            }
        }
        else {
            Alert.alert('0 Correct Answers!', 'You need to have at least one correct answer!', [{ text: 'Okay', style: 'destructive' }]);
        }
    }
    const sugestionButtonHandler = () => {
        if(questionHeader!="" || arrayAnswers[0].answer!="" || arrayAnswers[1].answer!="" || arrayAnswers[2].answer!="" || arrayAnswers[3].answer!=""){
            Alert.alert('Your fields will be replaced!', 'We will autocomplete the fields with a new question, are you sure you want to continue ?',[{text: 'Continue',onPress: getSuggestion},{text: 'Cancel'}],{cancelable: false});
        }else{
            var randomIndex = Math.round(Math.random() * (QUESTIONS.length - 1));
            setQuestionHeader(QUESTIONS[randomIndex].question);
            setArrayAnswers(QUESTIONS[randomIndex].answers);
            setNumCorrectAnswers(numCorrectAnswers + 1)
        }   
    }
    const getSuggestion = () => {
        var randomIndex = Math.round(Math.random() * (QUESTIONS.length - 1));
        setQuestionHeader(QUESTIONS[randomIndex].question);
        setArrayAnswers(QUESTIONS[randomIndex].answers);
        setNumCorrectAnswers(numCorrectAnswers + 1)
    }

    return (
        <TouchableWithoutFeedback onPress={() => { Keyboard.dismiss() }}>
            <View style={DefaultStyles.screen}>
                <View style={styles.questionContainer}>
                    <Text style={styles.plainText}>Question:</Text>
                    <Input
                        blurOnSubmit
                        autoCapitalize='none'
                        autoCorrect={false}
                        keyboardType='default'
                        onChangeText={questionHeaderInputHandler}
                        value={questionHeader}
                        multiline={true}
                        style={styles.questionText}
                    />
                </View>
                <Text style={styles.plainText}>Answers:</Text>
                    <View style={styles.answersContainer} >
                        <AnswerInput
                            blurOnSubmit
                            autoCapitalize='none'
                            autoCorrect={false}
                            keyboardType='default'
                            onChangeText={(inputText) => answerInputHandler(inputText, 0)}
                            value={arrayAnswers[0].answer}
                            onSelectAnswer={() => selectedAnswerHandler(0)}
                            teste={styles.teste}
                            check={arrayAnswers[0].bool}
                        />
                        <AnswerInput
                            blurOnSubmit
                            autoCapitalize='none'
                            autoCorrect={false}
                            keyboardType='default'
                            onChangeText={(inputText) => answerInputHandler(inputText, 1)}
                            value={arrayAnswers[1].answer}
                            onSelectAnswer={() => selectedAnswerHandler(1)}
                            teste={styles.teste}
                            check={arrayAnswers[1].bool}
                        />
                    
                        <AnswerInput
                            blurOnSubmit
                            autoCapitalize='none'
                            autoCorrect={false}
                            keyboardType='default'
                            onChangeText={(inputText) => answerInputHandler(inputText, 2)}
                            value={arrayAnswers[2].answer}
                            onSelectAnswer={() => selectedAnswerHandler(2)}
                            teste={styles.teste}
                            check={arrayAnswers[2].bool}
                        />
                        <AnswerInput
                            blurOnSubmit
                            autoCapitalize='none'
                            autoCorrect={false}
                            keyboardType='default'
                            onChangeText={(inputText) => answerInputHandler(inputText, 3)}
                            value={arrayAnswers[3].answer}
                            onSelectAnswer={() => selectedAnswerHandler(3)}
                            teste={styles.teste}
                            check={arrayAnswers[3].bool}
                        />    
                    </View>
                
                <View style={styles.btnsContainer}>
                    <View style={styles.topContainer}>
                        <Btn styleText={{ fontSize: Dimensions.get('window').height * 0.08 * 0.50 }} style={{ width: Dimensions.get("window").width * 0.540, height: Dimensions.get('window').height * 0.08 }} onPress={sugestionButtonHandler}>Autocomplete</Btn>
                        <Btn styleText={{ fontSize: Dimensions.get('window').height * 0.08 * 0.50 }} style={{ width: Dimensions.get("window").width * 0.270, height: Dimensions.get('window').height * 0.08 }} onPress={clearButtonHandler}>Clear</Btn>
                    </View>
                    <View style={styles.botContainer}>
                        <Btn styleText={{ fontSize: Dimensions.get('window').height * 0.08 * 0.50 }} style={{ width: Dimensions.get("window").width * 0.853, height: Dimensions.get('window').height * 0.08 }} onPress={saveButtonHandler}>Save</Btn>
                    </View>

                </View>

            </View>
        </TouchableWithoutFeedback>
    )
};

AddQuestionScreen.navigationOptions = {
    headerTitle: 'New Question'
};

const styles = StyleSheet.create({
    plainText: {
        fontSize: Dimensions.get("window").height * 0.07 * 0.42,
        fontFamily: 'archivo-bold',
        color: 'white'
    },
    answersContainer: {
        flex: 2.5,
        justifyContent: 'space-around',
        width: '90%'
    },
    btnsContainer: {
        flex: 1,
        flexDirection: 'column',
        alignItems: 'center',
        width: '90%',
        justifyContent: 'space-around',
    },
    questionContainer: {
        flex: 0.8,
        alignItems: 'center',
        marginBottom: 10,
        width: '90%'
    },
    questionText: {
        width: Dimensions.get("window").width * 0.88,
        height: Dimensions.get("window").height * 0.10,
        textAlignVertical: 'top',
        paddingHorizontal: 8,
        paddingVertical: 4
    },
    topContainer: {
        flex: 1,
        width: '95%',
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    botContainer: {
        flex: 1,
    },

});

export default AddQuestionScreen;
