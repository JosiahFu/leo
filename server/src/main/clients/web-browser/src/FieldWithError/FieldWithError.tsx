import './FieldWithError.scss';
import {ChangeEvent, HTMLInputTypeAttribute} from 'react';

export function FieldWithError<T>(props: {
  id: string;
  value: T;
  setValue: (value: T) => void;
  autoComplete: string;
  onBlur: () => void;
  type: HTMLInputTypeAttribute;
  maxLength: number;
  error: string | null | undefined;
}) {
  function valueChanged(event: ChangeEvent<HTMLInputElement>): void {
    props.setValue(event.target.value as T);
  }

  return (
    <div className={(props.error && 'field_with_error') || ''}>
      <input
        type={props.type}
        maxLength={props.maxLength}
        onChange={valueChanged}
        onBlur={props.onBlur}
        value={props.value as string}
        autoComplete={props.autoComplete}
      />
      {props.error && (
        <>
          <div>{props.error}</div>
        </>
      )}
    </div>
  );
}
