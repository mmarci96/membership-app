import { title } from "@/components/primitives";
import { SignupForm } from "@/components/signup-form";
import DefaultLayout from "@/layouts/default";

export default function DocsPage() {
    return (
        <DefaultLayout>
            <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
                <div className="inline-block max-w-lg text-center justify-center">
                    <h1 className={title()}>Create an account</h1>
                </div>
                <div className="m-4 w-full">
                    <SignupForm />
                </div>
            </section>
        </DefaultLayout>
    );
}
