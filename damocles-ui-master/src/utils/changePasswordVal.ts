import { ref } from "vue";

interface ChangePwdUser {
    oldPassowrd: string;
    password: string;
    password2: string;
}

export const user = ref<ChangePwdUser>({
    oldPassowrd: "",
    password: "",
    password2: "",
});

interface ChangePwdRules {
    oldPassowrd: ({
        required: boolean;
        message: string;
        trigger: string;
    } | {
        min: number;
        max: number;
        message: string;
        trigger: string;
    })[];
    password: ({
        required: boolean;
        message: string;
        trigger: string;
    } | {
        min: number;
        max: number;
        message: string;
        trigger: string;
    })[];
    password2: ({
        required: boolean;
        message: string;
        trigger: string;

    } | {
        min: number;
        max: number;
        message: string;
        trigger: string;

    } | {
        validator: (rule: ChangePwdRules, value: string, callback: Function) => void;
        trigger: string;
    })[];
}

const validatePass2 = (rule: ChangePwdRules, value: string, callback: Function) => {
    if (value === "") {
        callback(new Error("请再次输入密码"));
    } else if (value !== user.value.password) {
        callback(new Error("两次输入密码不一致!"));
    } else {
        callback();
    }
};

export const rules = ref<ChangePwdRules>({
    oldPassowrd: [
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
    password2: [
        {
            required: true,
            message: "Password2 could not be empty...",
            trigger: "blur",
        },
        {
            min: 6,
            max: 16,
            message: "Password's length has to be 6 to 16 characters...",
            trigger: "blur",
        },
        { validator: validatePass2, trigger: "blur" },
    ]

});