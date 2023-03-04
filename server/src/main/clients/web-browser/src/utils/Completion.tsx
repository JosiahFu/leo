import { stringSet } from './Venn';

interface CompletionResponse {
    completion: string;
}

async function generateCompletion(input: stringSet): Promise<stringSet> {
    const prompt = `Give me 4 project ideas for high school that develop and assess the skill (${input[1]}) that uses ${input[2]} and connects to ${input[3]} and a career of ${input[0]}. Make sure to separate the ideas with a new line and steer clear of any fluff.`;
    const response = await fetch('/generateCompletion', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ prompt })
    });
    const data: CompletionResponse = await response.json();
    const completion = data.completion.split("\n\n", 4);
    return [
        completion[0],
        completion[1],
        completion[2],
        completion[3],
    ];
}

export default generateCompletion;