import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import translations from './utils/translationFile.json';

const resources = translations;

export const keys = Object.keys(resources);
const defaultLanguage = localStorage.getItem('language') || keys[0];

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    resources,//define resources
    lng: defaultLanguage,//define the first key in our Json file as the default

    keySeparator: false, // we do not use keys in form messages.welcome

    interpolation: {
      escapeValue: false // react already safes from xss
    }
  });

  export default i18n;