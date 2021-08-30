import { ref } from "vue";

interface RegisterUser {
    nickName: string;
    password: string;
    password2: string;
    email: string;
}

export const registerUser = ref<RegisterUser>({
    nickName: "",
    email: "",
    password: "",
    password2: "",
});

interface RegisterRules {
    nickName: ({
        message: string;
        required: boolean;
        trigger: string;
    } | {
        min: number;
        max: number;
        message: string;
        trigger: string;
    })[];
    email: {
        type: string;
        message: string;
        required: boolean;
        trigger: string;
    }[];
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
        validator: (rule: RegisterRules, value: string, callback: Function) => void;
        trigger: string;
    })[];
}

const validatePass2 = (rule: RegisterRules, value: string, callback: Function) => {
    if (value === "") {
        callback(new Error("请再次输入密码"));
    } else if (value !== registerUser.value.password) {
        callback(new Error("两次输入密码不一致!"));
    } else {
        callback();
    }
};

export const registerRules = ref<RegisterRules>({
    nickName: [
        {
            message: "用户名不能为空...",
            required: true,
            trigger: "blur",
        },
        {
            min: 2,
            max: 10,
            message: "长度在2到10个字符",
            trigger: "blur",
        },
    ],
    email: [
        {
            type: "email",
            message: "Email is incorrect...",
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