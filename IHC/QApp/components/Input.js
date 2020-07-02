import React from 'react';
import { TextInput,Dimensions, StyleSheet } from 'react-native';
import Colors from "../constants/colors";
import Aspect from '../constants/aspect';

const Input = props => {
    return (
        <TextInput {...props} style={{ ...styles.input, ...props.style }}/>
    )
};

const styles = StyleSheet.create({
    input:{
        width: Dimensions.get("window").width * 0.65,
        height: Dimensions.get("window").height * 0.07,
        backgroundColor: "white",
        borderRadius: 12,
        borderWidth: 3,
        borderColor: Colors.bordercolor,
        fontFamily: "archivo-regular",
        fontStyle: "normal",
        fontSize: Dimensions.get("window").height * 0.07 * 0.42,
        color: Colors.inputcolor,
        justifyContent: "center",
        paddingHorizontal: 8,
        marginTop: Dimensions.get("window").height * Aspect.marginValue
    }
});

export default Input;
