// To parse this data:
//
//   import { Convert, Questions } from "./file";
//
//   const questions = Convert.toQuestions(json);

export interface Questions {
    questions: Question[];
}

export interface Question {
    question: CorrectAnswer;
    answers: Answers;
    correct_answer: CorrectAnswer;
    explanation: CorrectAnswer;
}

export interface Answers {
    fr: string[];
    en: string[];
}

export interface CorrectAnswer {
    fr: string;
    en: string;
}

// Converts JSON strings to/from your types
export class Convert {
    public static toQuestions(json: string): Questions {
        return JSON.parse(json);
    }

    public static questionsToJson(value: Questions): string {
        return JSON.stringify(value);
    }
}
