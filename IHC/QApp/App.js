import React, { useState } from 'react';
import * as Font from 'expo-font';
import { AppLoading } from 'expo';
import { useScreens } from 'react-native-screens';
import MenuNavigator from './navigation/MenuNavigator'
import { YellowBox , StatusBar} from 'react-native';
import { InAppNotificationProvider } from 'react-native-in-app-notification';

import { createStore, combineReducers } from 'redux';
import gameReducer from './store/reducers/game';
import { Provider } from 'react-redux';


useScreens();

const rootReducer = combineReducers({
  game: gameReducer
});

const store = createStore(rootReducer);



const fetchFonts = () => {
  return Font.loadAsync({
    'archivo-regular': require('./assets/fonts/Archivo-Regular.ttf'),
    'archivo-bold': require('./assets/fonts/Archivo-Bold.ttf'),
  });
}


export default function App() {

  YellowBox.ignoreWarnings([
    'Unrecognized WebSocket connection option(s) `agent`, `perMessageDeflate`, `pfx`, `key`, `passphrase`, `cert`, `ca`, `ciphers`, `rejectUnauthorized`. Did you mean to put these under `headers`?'
  ]);
  const [fontLoaded, setFontLoaded] = useState(false);
  if (!fontLoaded) {
    return (
      <AppLoading startAsync={fetchFonts}
        onFinish={() => setFontLoaded(true)}
        onError={err => console.log(err)} />
    )
  }
  return (
    <Provider store={store}>
      <InAppNotificationProvider>
        <MenuNavigator />
      </InAppNotificationProvider>
    </Provider>

  );
}
