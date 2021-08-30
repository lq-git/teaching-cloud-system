import { ref } from "vue";

export interface User {
    username: string;
    password: string;
    verifyCode: string;
}

export const loginUser = ref<User>({
    username: "",
    password: "",
    verifyCode: ""
});

interface Rules {
    username: {
        type: string;
        message: string;
        required: boolean;
        trigger: string;
    }[];
    password: ({
        required: boolean;
        message: string;
        trigger: string;
        min?: undefined;
        max?: undefined;
    } | {
        min: number;
        max: number;
        message: string;
        trigger: string;
        required?: undefined;
    })[];
}

// 校验规则
export const rules = ref<Rules>({
    username: [
        {
            type: "email",
            message: "username is incorrect...",
            required: true,
            trigger: "blur",
        },
    ],
    password: [
        {
            required: true,
            message: "Password could not be empty...",
            trigger: "blur",
        },
        {
            min: 6,
            max: 16,
            message: "Password's length has to be 6 to 16 characters...",
            trigger: "blur",
        },
    ],
});