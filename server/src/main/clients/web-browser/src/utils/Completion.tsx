import { Configuration, OpenAIApi } from 'openai';
import { stringSet } from './Venn';
import getAPIKey from './APIKey';

async function getCompletion(input: stringSet): Promise<stringSet> {
    const configuration = new Configuration({
        apiKey: getAPIKey(), 
    });
    const openai = new OpenAIApi(configuration);

    const completion = await openai.createChatCompletion({
        model: "gpt-3.5-turbo",
        messages: [
            {role: "system", content: "You are a creative and helpful assistant that specializes in creating high school project ideas"},
            {role: "user", content: `Give me 4 project ideas for high school that develop and assess the skill (${input[1]}) that uses ${input[2]} and connects to ${input[3]} and a career of ${input[0]}. Make sure to separate the ideas with a new line and steer clear of any fluff.`},
        ],
    });

    console.log(completion);
    const result = completion?.data?.choices[0]?.message?.content?.split("\n\n", 4) as string[] | undefined;
    console.log(result);
    if (result === undefined) {
        return ['', '', '', ''];
    }
    return [
        result[0],
        result[1],
        result[2],
        result[3],
    ]
}

export default getCompletion;