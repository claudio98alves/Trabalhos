import React from "react";
import {StyleSheet, Text, TouchableOpacity, Dimensions} from 'react-native';
import Colors from '../constants/colors';

const Btn = props => {
    return(
    <TouchableOpacity onPress={props.onPress} activeOpacity={0.6} style={{...styles.btn,...props.style}}>
        <Text style={{...styles.titleStyle,...props.styleText}}>{props.children}</Text>
    </TouchableOpacity> 
    );
};


const styles = StyleSheet.create({
    btn: {
        backgroundColor: Colors.btncolor,
        borderRadius: 12,
        borderWidth: 3,
        borderColor: Colors.bordercolor,
        alignItems: "center",
        justifyContent: "center",
        width: Dimensions.get('window').width * 0.65,
        height: Dimensions.get('window').height * 0.1
    },
    titleStyle: {
        fontFamily: 'archivo-bold', 
        fontStyle: 'normal',
        textAlign: 'center',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#ffffff',
        fontSize: Dimensions.get('window').height * 0.1 * 0.50
    }
});

export default Btn;
