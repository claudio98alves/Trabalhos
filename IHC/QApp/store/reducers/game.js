import { ADD_QUESTION, EDIT_QUESTION, RESET_STATE } from "../actions/game";


const initialState = {
    questionsAdded: [],
};

const gameReducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_QUESTION:
            var updatedQuestions = [...state.questionsAdded];
            updatedQuestions.push(action.question);
            return { ...state, questionsAdded: updatedQuestions }
        case EDIT_QUESTION:
            var updatedQuestions = [...state.questionsAdded];
            updatedQuestions[action.index] = action.question;
            return { ...state, questionsAdded: updatedQuestions };
        case RESET_STATE:
            return initialState;
        default:
            return state;
    }
}

export default gameReducer;
