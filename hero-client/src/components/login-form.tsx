import React from "react";
import { Form, Input, Checkbox, Button } from "@heroui/react";

type FormValues = {
    email: string;
    password: string;
};

type FormErrors = Partial<Record<keyof FormValues | "terms", string>>;

export const LoginForm = () => {
    const [submitted, setSubmitted] = React.useState<FormValues | null>(null);
    const [errors, setErrors] = React.useState<FormErrors>({});

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const data = Object.fromEntries(formData.entries()) as FormValues;

        const newErrors: FormErrors = {};

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }
        setErrors({});

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
                    errorMessage={({ validationDetails }) => {
                        if (validationDetails.valueMissing) {
                            return "Please enter your password";
                        }
                        return errors.email;
                    }}
                    label="Password"
                    labelPlacement="outside"
                    name="password"
                    placeholder="Enter your password"
                    type="password"
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
                    Stay logged in
                </Checkbox>

                {errors.terms && (
                    <span className="text-danger text-small">
                        {errors.terms}
                    </span>
                )}

                <div className="flex gap-4">
                    <Button className="w-full" color="primary" type="submit">
                        Create Account
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
