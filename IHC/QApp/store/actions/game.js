export const ADD_QUESTION = 'ADD_QUESTION';
export const EDIT_QUESTION = 'EDIT_QUESTION';
export const RESET_STATE = "RESET_STATE";

export const addQuestion = (question) => {
    return { type: ADD_QUESTION, question: question };
}

export const editQuestion = (question, index) => {
    return { type: EDIT_QUESTION, question: question, index: index };
}

export const resetState = () =>{
    return {type: RESET_STATE}
}


