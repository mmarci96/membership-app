import React from "react";
import {
    Form,
    Input,
    Select,
    SelectItem,
    Checkbox,
    Button,
} from "@heroui/react";

type FormValues = {
    name: string;
    email: string;
    password: string;
    country: string;
    terms?: string;
};

type FormErrors = Partial<Record<keyof FormValues | "terms", string>>;

export const AuthForm = () => {
    const [password, setPassword] = React.useState("");
    const [submitted, setSubmitted] = React.useState<FormValues | null>(null);
    const [errors, setErrors] = React.useState<FormErrors>({});

    const getPasswordError = (value: string): string | null => {
        if (value.length < 8) {
            return "Password must be 8 characters or more";
        }
        if (!/[A-Z]/.test(value)) {
            return "Password needs at least 1 uppercase letter";
        }
        if (!/[0-9]/.test(value)) {
            return "Password needs at least 1 number";
        }
        return null;
    };

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const data = Object.fromEntries(formData.entries()) as FormValues;

        const newErrors: FormErrors = {};

        const passwordError = getPasswordError(data.password);
        if (passwordError) {
            newErrors.password = passwordError;
        }

        if (data.name === "admin") {
            newErrors.name = "Nice try! Choose a different username";
        }

        if (!data.terms) {
            newErrors.terms = "Please accept the terms";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        setErrors({});
        console.log("Submit data: ", data);

        setSubmitted(data);
    };

    return (
        <Form
            className="w-full justify-center items-center space-y-4 mb-4"
            validationErrors={errors}
            onReset={() => setSubmitted(null)}
            onSubmit={onSubmit}
        >
            <div className="flex flex-col gap-4 max-w-md">
                <Input
                    isRequired
                    errorMessage={({ validationDetails }) => {
                        if (validationDetails.valueMissing) {
                            return "Please enter your name";
                        }
                        return errors.name;
                    }}
                    label="Name"
                    labelPlacement="outside"
                    name="name"
                    placeholder="Enter your name"
                />

                <Input
                    isRequired
                    errorMessage={({ validationDetails }) => {
                        if (validationDetails.valueMissing) {
                            return "Please enter your email";
                        }
                        if (validationDetails.typeMismatch) {
                            return "Please enter a valid email address";
                        }
                        return errors.email;
                    }}
                    label="Email"
                    labelPlacement="outside"
                    name="email"
                    placeholder="Enter your email"
                    type="email"
                />

                <Input
                    isRequired
                    errorMessage={getPasswordError(password)}
                    isInvalid={getPasswordError(password) !== null}
                    label="Password"
                    labelPlacement="outside"
                    name="password"
                    placeholder="Enter your password"
                    type="password"
                    value={password}
                    onValueChange={setPassword}
                />
                <Checkbox
                    isRequired
                    classNames={{
                        label: "text-small",
                    }}
                    isInvalid={!!errors.terms}
                    name="terms"
                    validationBehavior="aria"
                    value="true"
                    onValueChange={() =>
                        setErrors((prev) => ({ ...prev, terms: undefined }))
                    }
                >
                    I agree to the terms and conditions
                </Checkbox>

                {errors.terms && (
                    <span className="text-danger text-small">
                        {errors.terms}
                    </span>
                )}

                <div className="flex gap-4">
                    <Button className="w-full" color="primary" type="submit">
                        Submit
                    </Button>
                    <Button type="reset" variant="bordered">
                        Reset
                    </Button>
                </div>
            </div>

            {submitted && (
                <div className="text-small text-default-500 mt-4">
                    Submitted data:{" "}
                    <pre>{JSON.stringify(submitted, null, 2)}</pre>
                </div>
            )}
        </Form>
    );
};
