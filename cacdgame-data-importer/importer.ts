import { LogLevel, SchemaTypes, SimpleSchemaTypes, buildClient } from "@datocms/cma-client-node";
import { parse } from "ts-command-line-args";
import { readFileSync } from "fs";
import { Answers, Convert, CorrectAnswer, Questions } from "./jsonImporter";

//content check
const categories = { "design": 105396726, "produit": 105396727, "tech": 105396728 }
const difficulties = ["Débutant", "Confirmé", "Incollable"]
const languages: String[] = ["fr", "en"]

// input parser
interface ICopyFilesArguments {
    category: String;
    filePath: String;
    difficulty: String;
}

export const args = parse<ICopyFilesArguments>({
    category: String,
    filePath: String,
    difficulty: String
});

// input check

const indexOfCategory = Object.keys(categories).indexOf(args.category.toString().toLowerCase())
if (Object.keys(categories).indexOf(args.category.toString().toLowerCase()) === -1) {
    throw "Bad supported category, must be one [" + Object.keys(categories) + "]";
}

const selectedDifficulty = args.difficulty.toString()

if (difficulties.includes(selectedDifficulty) === false) {
    throw "Bad supported difficulty, must be one [" + difficulties + "]";
}

const categoryId = Object.values(categories)[indexOfCategory]
const file = readFileSync(args.filePath.toString(), "utf-8");
const importedQuestions = Convert.toQuestions(file);

const client = buildClient(
    {
        apiToken: "ae70ad53b8ab16017a4ef7709df81e",
        logLevel: LogLevel.BASIC
    }
);

const categoryItemTypeId = "1408126"
const questionsItemTypeId = "1408123"
const propositionItemTypeId = "1408128"

async function run() {

    const prompts = require("prompts");

    const response = await (async () => {
        return await prompts({
            type: "confirm",
            name: "meaning",
            message: "L'import des questions de la catégorie va supprimer les anciennes sur le serveur"
        });
    })();

    if (response.meaning === false) {
        return
    }


    const foundQuestions = await client.items.list({
        filter: {
            type: "question",
            fields: {
                category: {
                    eq: categoryId
                },
                difficulty: {
                    eq: selectedDifficulty
                }
            }
        }
    })
    if (foundQuestions.length > 0) {
        await deleteQuestions(foundQuestions)
    }
    const total = await createQuestions(importedQuestions)
    console.log("ADDED RECORDS : " + total.length)
}

async function deleteQuestions(questions: SimpleSchemaTypes.ItemInstancesTargetSchema) {
    let toDeleteArray: Object[] = []
    for (const value of questions) {
        console.log("Deleting Question Id " + value.id)
        toDeleteArray.push({
            type: 'item',
            id: value.id
        })
    }
    await client.items.bulkDestroy({
        items: toDeleteArray
    } as SimpleSchemaTypes.ItemBulkDestroySchema)
}

async function createQuestions(importedQuestions: Questions): Promise<SimpleSchemaTypes.ItemCreateSchema[]> {
    let questionsAdded: SimpleSchemaTypes.ItemCreateSchema[] = []
    interface propositionType {  // 👈 typing for the "romanNumber" object
        [key: string]: any;
    }
    for (const question of importedQuestions.questions) {
        let propositions: propositionType = {}
        const langs = Object.keys(question.answers)

        for (const lang of langs) {
            propositions[lang] = []
            let answerIndex = 0
            while (answerIndex < 4) {
                const propositionMsg = question.answers[lang as keyof Answers]
                const correctAnswer = question.correct_answer[lang as keyof CorrectAnswer]

                const newProposition = {
                    "type": "item",
                    "attributes": {
                        "proposition": propositionMsg[answerIndex],
                        "correct": propositionMsg[answerIndex] === correctAnswer
                    },
                    "relationships": {
                        "item_type": {
                            "data": {
                                // the block model
                                "id": propositionItemTypeId,
                                "type": "item_type",
                            }
                        }
                    }
                }
                propositions[lang].push(newProposition)
                answerIndex++;
            }
        }
        const questionToAdd = {
            "label": question.question,
            "category": categoryId.toString(),
            "explanation": question.explanation,
            "item_type": { id: questionsItemTypeId, type: "item_type" },
            "difficulty": selectedDifficulty,
            "choices": propositions
        } as SimpleSchemaTypes.ItemCreateSchema
        try {
            questionsAdded.push(await client.items.create(questionToAdd))
        } catch (error) {
            console.error(error)
            console.error("CAN PROCESS \n" + JSON.stringify(questionToAdd))
        }
    }
    return questionsAdded
}

run()