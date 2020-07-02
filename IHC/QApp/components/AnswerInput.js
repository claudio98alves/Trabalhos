import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Keyboard } from 'react-native';
import Input from '../components/Input';
import { Ionicons } from '@expo/vector-icons';

const AnswerInput = props => {
    return (
        <View style={styles.screen}>
            <Input {...props} style={{ borderColor: props.check ? 'green' : 'red', width: '88%', marginTop: 0 }} />
            <TouchableOpacity onPress={props.onSelectAnswer}>
                <View style={styles.symbolContainer}>
                    <Ionicons name={props.check ? 'ios-checkmark-circle' : 'ios-close-circle'}
                        color={props.check ? 'green' : 'red'}
                        size={24}
                    />
                </View>
            </TouchableOpacity>
        </View>

    );
};

const styles = StyleSheet.create({
    screen: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between'
    },
    symbolContainer: {
        justifyContent: 'center',
    }
});
export default AnswerInput;