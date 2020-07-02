import { StyleSheet,Dimensions } from 'react-native';
import Aspect from '../constants/aspect';
import Colors from '../constants/colors';

export default StyleSheet.create({
  title: {
    fontFamily: 'archivo-bold',
    fontSize: Dimensions.get("window").height * 0.04,
    color: "white",
    marginTop: 20,
  },
  screen: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: Colors.bckcolor,
    paddingTop: Dimensions.get("window").height * Aspect.marginValue,
    paddingBottom: Dimensions.get("window").height * Aspect.marginValue,
}
});
